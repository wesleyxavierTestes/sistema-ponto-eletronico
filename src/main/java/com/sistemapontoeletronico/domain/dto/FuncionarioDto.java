package com.sistemapontoeletronico.domain.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.JoinColumn;

import com.sistemapontoeletronico.domain.entities.biometria.Biometria;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioSetor;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioDto {

    @NonNull
    @JoinColumn(name = "nome")
    public String nome;

    @NonNull
    @JoinColumn(name = "acesso")
    public String acesso;

    @JoinColumn(name = "acessoBloqueado")
    public boolean acessoBloqueado;

    @NonNull
    @JoinColumn(name = "funcionarioEstado")
    public EnumFuncionarioEstado funcionarioEstado;

    @NonNull
    @JoinColumn(name = "funcionarioSetor")
    public EnumFuncionarioSetor funcionarioSetor;

}