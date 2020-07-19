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
        funcionarioPadrao.funcionarioSetor = EnumFuncionarioSetor.RH;
        funcionarioPadrao.nome = "Admin";
        funcionarioPadrao.acesso = "SecretAdmin123";

        return funcionarioPadrao;
    }

    private String nome;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    private List<Biometria> biometrias;

    private String acesso;

    @Enumerated(EnumType.STRING)
    private EnumFuncionarioEstado FuncionarioEstado;

    @Enumerated(EnumType.STRING)
    private EnumFuncionarioSetor funcionarioSetor;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<RelogioPonto> RelogiosPonto;

    public boolean ValidarSetorEspecifico(EnumFuncionarioSetor setor) {
        boolean valido = this.funcionarioSetor == setor;
        return valido;
    }
}