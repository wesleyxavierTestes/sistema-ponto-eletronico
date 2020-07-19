package com.sistemapontoeletronico.domain.entities;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;

import org.junit.jupiter.api.Test;

public class RelogioPontoTest {

    @Test
    void NoHorario_ValidarEstaAtrasadoTest() {
        LocalDateTime finalExpediente = LocalDateTime.now();
        LocalDateTime ponto = LocalDateTime.now();

        PreDefinicaoPonto preDefinicao = new PreDefinicaoPonto();
        preDefinicao.setFinalExpediente(finalExpediente);
        preDefinicao.setLimiteAtrasoSemana(5);
        
        RelogioPonto relogioPonto = new RelogioPonto();
        
        relogioPonto.setPonto(ponto);
        
        boolean atrasado = relogioPonto.ValidarEstaAtrasado(preDefinicao);

        assertTrue(!atrasado, "");
    }
    
}