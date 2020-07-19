package com.sistemapontoeletronico.domain.entities.preDefinicaoPonto;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import com.sistemapontoeletronico.domain.entities.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PreDefinicaoPonto extends BaseEntity {
    
    private int limiteAtrasoSemana;
    private int minutosTolerancia;
    //private LocalDateTime InicioExpediente;
    //private LocalDateTime InicioDescanso;
   // private LocalDateTime FinalDescanso;
    private LocalDateTime FinalExpediente;
}