package com.sistemapontoeletronico.domain.entities;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

public class Acesso {

    @NonNull
    public String codigoAcesso;
    @NonNull
    public LocalDateTime ponto;
    
}