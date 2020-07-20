package com.sistemapontoeletronico.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;

import com.sistemapontoeletronico.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RelogioPontoTest {
private RelogioPonto relogioPonto;
private PreDefinicaoPonto preDefinicao;

    @BeforeEach
    void setup() {
        relogioPonto = new RelogioPonto();
        preDefinicao = new PreDefinicaoPonto();
    }

    @ParameterizedTest
    @CsvSource({
            "5,0,false",
            "5,300,false",
            "5,299,false",
            "5,301,true",

            "2,0,false",
            "2,120,false",
            "2,299,true",
            "2,301,true",

            "10,0,false",
            "10,300,false",
            "100,299,false",
            "10,301,false",
            "10,601,true",
    })
    void NoHorario_ValidarEstaAtrasadoTest(int toleranciaMinutos, int totalSegundosAtraso, boolean estaAtrasado) {
        LocalDateTime expediente = LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(),
                LocalDateTime.now().getHour(),
                LocalDateTime.now().getMinute(),
                0
        );
        long pontoMili = DateUtils.GetMilisegundosPorSegundos(expediente, totalSegundosAtraso);
        LocalDateTime ponto = DateUtils.GetLocalDateTime(pontoMili);

        preDefinicao.setFinalExpediente(expediente);
        preDefinicao.setMinutosTolerancia(toleranciaMinutos);
        relogioPonto.setPonto(ponto);
        
        boolean atrasado = relogioPonto.ValidarEstaAtrasadoInicio(
                preDefinicao.getFinalExpediente(),
                preDefinicao.getMinutosTolerancia());

        assertEquals(estaAtrasado, atrasado, "");
    }
}