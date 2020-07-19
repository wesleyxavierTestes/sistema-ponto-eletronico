package com.sistemapontoeletronico.domain.entities.relogioPonto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sistemapontoeletronico.domain.entities.BaseEntity;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;

import com.sistemapontoeletronico.utils.DateUtils;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RelogioPonto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    private Funcionario funcionario;
    private LocalDateTime ponto;
    private boolean estaAtrasado;

    public boolean ValidarEstaAtrasado(PreDefinicaoPonto preDefinicao) {
        long toleranciaEmMilisegundos = DateUtils.GetMilisegundos(preDefinicao.getMinutosTolerancia());

        long finalExpediente = DateUtils.GetMilisegundos(preDefinicao.getFinalExpediente());
        long pontoMinimo = DateUtils.GetMilisegundos(this.ponto, - toleranciaEmMilisegundos);
        long pontoMaximo = DateUtils.GetMilisegundos(this.ponto, toleranciaEmMilisegundos);

        boolean ok = finalExpediente > pontoMinimo && finalExpediente < pontoMaximo;

        return ok;
    }


}