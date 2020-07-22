package com.sistemapontoeletronico.domain.dto;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

public class PreDefinicaoPontoDto {
    private final long limiteBatidas = 2;

    private int limiteQuantidadeAtrasoSemana;

    private int minutosTolerancia;
    
    private int quantidadePontosDia;

    @NonNull
    private LocalDateTime inicioExpediente;

    @NonNull
    private LocalDateTime inicioDescanso;

    @NonNull
    private LocalDateTime finalDescanso;

    @NonNull
    private LocalDateTime finalExpediente;
}