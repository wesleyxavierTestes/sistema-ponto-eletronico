package com.sistemapontoeletronico.domain.dto;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

public class AcessoDto {

    @NonNull
    public String codigoAcesso;
    @NonNull
    public LocalDateTime ponto;
    
}