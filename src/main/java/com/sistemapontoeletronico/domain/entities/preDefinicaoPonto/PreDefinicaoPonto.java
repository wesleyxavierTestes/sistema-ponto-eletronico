package com.sistemapontoeletronico.domain.entities.preDefinicaoPonto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.sistemapontoeletronico.domain.entities.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PreDefinicaoPonto extends BaseEntity {

    private final long limiteBatidas = 2;

    private int limiteQuantidadeAtrasoSemana;

    private int minutosTolerancia;
    
    private int quantidadePontosDia;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime inicioExpediente;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime inicioDescanso;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime finalDescanso;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime finalExpediente;
}