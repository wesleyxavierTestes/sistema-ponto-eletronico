package com.sistemapontoeletronico.domain.entities.relogioPonto;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.sistemapontoeletronico.domain.entities.BaseEntity;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;

import com.sistemapontoeletronico.domain.enuns.EnumRelogioPontoEstado;
import com.sistemapontoeletronico.utils.DateUtils;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RelogioPonto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    private Funcionario funcionario;

    @Column(nullable = false)
    private LocalDateTime ponto;

    private EnumRelogioPontoEstado relogioPontoEstado;
    private boolean inconsistente;

    public EnumRelogioPontoEstado ValidarEstaAtrasadoInicio(LocalDateTime expediente, long toleranciaMinutos) {
        long toleranciaEmMilisegundos = DateUtils.GetMilisegundosPorMinutos(toleranciaMinutos);
        long pontoAtual = DateUtils.GetMilisegundos(this.ponto);

        long toleranciaMinimo = DateUtils.GetMilisegundos(expediente, - toleranciaEmMilisegundos);
        long toleranciaMaximo = DateUtils.GetMilisegundos(expediente, toleranciaEmMilisegundos);

        if (pontoAtual < toleranciaMinimo) {
            return EnumRelogioPontoEstado.Adiantado;
        }
        else if (pontoAtual > toleranciaMaximo) {
            return EnumRelogioPontoEstado.Atrasado;
        }

        return EnumRelogioPontoEstado.NoHorario;
    }

    public EnumRelogioPontoEstado ValidarEstaAtrasadoFinal(LocalDateTime expediente, long toleranciaMinutos) {
        long toleranciaEmMilisegundos = DateUtils.GetMilisegundosPorMinutos(toleranciaMinutos);
        long pontoAtual = DateUtils.GetMilisegundos(this.ponto);

        long toleranciaMinimo = DateUtils.GetMilisegundos(expediente, - toleranciaEmMilisegundos);
        long toleranciaMaximo = DateUtils.GetMilisegundos(expediente, toleranciaEmMilisegundos);

        if (pontoAtual < toleranciaMinimo) {
            return EnumRelogioPontoEstado.Atrasado;
        }
        else if (pontoAtual > toleranciaMaximo) {
            return EnumRelogioPontoEstado.Adiantado;
        }

        return EnumRelogioPontoEstado.NoHorario;
    }
}