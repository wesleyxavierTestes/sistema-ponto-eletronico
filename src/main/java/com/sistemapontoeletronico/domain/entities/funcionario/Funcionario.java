package com.sistemapontoeletronico.domain.entities.funcionario;

import javax.persistence.*;

import com.sistemapontoeletronico.domain.entities.BaseEntity;

import com.sistemapontoeletronico.domain.entities.biometria.Biometria;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioSetor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Funcionario extends BaseEntity {

    /* Usuário poderá ser usado se não houver nenhum cadastrado no sistema; */
    public static Funcionario FuncionarioPadrao() {
        Funcionario funcionarioPadrao = new Funcionario();
        funcionarioPadrao.funcionarioSetor = EnumFuncionarioSetor.Rh;
        funcionarioPadrao.nome = "Admin";
        funcionarioPadrao.acesso = "SecretAdmin123";

        return funcionarioPadrao;
    }

    @Column(nullable = false)
    private String nome;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    @Column(nullable = true)
    private List<Biometria> biometrias;

    @Column(nullable = true)
    private String acesso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumFuncionarioEstado funcionarioEstado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumFuncionarioSetor funcionarioSetor;

    @OneToMany(cascade = CascadeType.ALL)//, mappedBy="funcionario")
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    private List<RelogioPonto> relogiosPonto;

    public boolean ValidarSetorEspecifico(EnumFuncionarioSetor setor) {
        boolean valido = this.funcionarioSetor == setor;
        return valido;
    }

    public boolean EstaBloqueado() {
        return this.funcionarioEstado == EnumFuncionarioEstado.Bloqueado;
    }
}