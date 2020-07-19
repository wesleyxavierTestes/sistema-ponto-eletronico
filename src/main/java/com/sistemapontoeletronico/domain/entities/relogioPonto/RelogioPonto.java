package com.sistemapontoeletronico.domain.entities.relogioPonto;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sistemapontoeletronico.domain.entities.BaseEntity;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;

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

    public boolean ValidarEstaAtrasado(LocalDateTime expediente, long tolerancia) {
        boolean estaAtrasado = ValidarEstaAtrasado(tolerancia, expediente, this.ponto);
        return estaAtrasado;
    }

    private boolean ValidarEstaAtrasado(long toleranciaMinutos, LocalDateTime expediente, LocalDateTime novoPonto) {
        long toleranciaEmMilisegundos = DateUtils.GetMilisegundosPorMinutos(toleranciaMinutos);
        long pontoAtual = DateUtils.GetMilisegundos(novoPonto);

        long toleranciaMinimo = DateUtils.GetMilisegundos(expediente, - toleranciaEmMilisegundos);
        long toleranciaMaximo = DateUtils.GetMilisegundos(expediente, toleranciaEmMilisegundos);

        boolean estaAtrasdado = pontoAtual < toleranciaMinimo || pontoAtual > toleranciaMaximo;

        return estaAtrasdado;
    }


}