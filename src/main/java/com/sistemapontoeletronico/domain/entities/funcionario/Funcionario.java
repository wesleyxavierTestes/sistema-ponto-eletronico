package com.sistemapontoeletronico.domain.entities.funcionario;

import javax.persistence.*;

import com.sistemapontoeletronico.domain.entities.BaseEntity;

import com.sistemapontoeletronico.domain.entities.biometria.Biometria;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioSetor;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Funcionario extends BaseEntity {

    /**
     * Usuário poderá ser usado se não houver nenhum cadastrado no sistema;
     * @return Funcionario
     */
    public static Funcionario funcionarioPadrao() {
        Funcionario funcionarioPadrao = new Funcionario();
        funcionarioPadrao.funcionarioSetor = EnumFuncionarioSetor.Rh;
        funcionarioPadrao.nome = "administrador";
        funcionarioPadrao.acesso = "SecretAdmin123";

        return funcionarioPadrao;
    }

    @Column(nullable = false)
    @NonNull
    private String nome;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    @Column(nullable = true)
    private List<Biometria> biometrias;

    @Column(nullable = false, unique = true)
    @NonNull
    private String acesso;

    private boolean acessoBloqueado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private EnumFuncionarioEstado funcionarioEstado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private EnumFuncionarioSetor funcionarioSetor;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    private List<RelogioPonto> relogiosPonto;

    public boolean validarSetorEspecifico(EnumFuncionarioSetor setor) {
        return this.funcionarioSetor == setor;
    }

    public boolean validaEstaBloqueado() {
        return this.funcionarioEstado == EnumFuncionarioEstado.Bloqueado;
    }
}