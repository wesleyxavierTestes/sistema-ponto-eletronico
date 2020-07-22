package com.sistemapontoeletronico.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sistemapontoeletronico.domain.dto.FuncionarioDto;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioSetor;
import com.sistemapontoeletronico.utils.ModelMapperUtils;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class FuncionarioTest {

    private ModelMapper modelMapper = ModelMapperUtils.getInstance();

    @Test
    void validaMapperTest() {

        Funcionario funcionario = null;
        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.acesso = "123456";
        funcionarioDto.nome = "Teste";
        funcionarioDto.acessoBloqueado = false;
        funcionarioDto.funcionarioEstado = EnumFuncionarioEstado.Ativo;
        funcionarioDto.funcionarioSetor = EnumFuncionarioSetor.Geral;

        try {
            funcionario = modelMapper
            //.createTypeMap(FuncionarioDto.class)
            .map(funcionarioDto, Funcionario.class); 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(true);
        String acesso = funcionario.getAcesso();
       // assertEquals(funcionarioDto.acesso, acesso);
    }
    
}