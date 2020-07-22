package com.sistemapontoeletronico.domain.dto;

import java.time.LocalDateTime;

import com.sistemapontoeletronico.domain.enuns.EnumRelogioPontoEstado;

import org.springframework.lang.NonNull;

public class RelogioPontoDto {
    public FuncionarioDto funcionario;

    public String nomeFuncionario;

    @NonNull
    public LocalDateTime ponto;

    public EnumRelogioPontoEstado relogioPontoEstado;

    public boolean inconsistente;

    public int numeroPontoDia;
}