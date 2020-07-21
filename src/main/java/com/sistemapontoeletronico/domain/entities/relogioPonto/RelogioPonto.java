package com.sistemapontoeletronico.domain.entities.relogioPonto;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sistemapontoeletronico.domain.entities.BaseEntity;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;

import com.sistemapontoeletronico.domain.enuns.EnumRelogioPontoEstado;
import com.sistemapontoeletronico.utils.DateUtils;

import org.springframework.lang.NonNull;

import lombok.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RelogioPonto extends BaseEntity {

    public RelogioPonto(LocalDateTime ponto, String nomeFuncionario) {
        super();
        this.setPonto(ponto);
        this.setNomeFuncionario(nomeFuncionario);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    @JsonIgnore
    protected Funcionario funcionario;

    protected String NomeFuncionario;

    @Column(nullable = false)
    @NonNull
    protected LocalDateTime ponto;

    @Enumerated(EnumType.STRING)
    protected EnumRelogioPontoEstado relogioPontoEstado;

    protected boolean inconsistente;

    protected int numeroPontoDia;

    public EnumRelogioPontoEstado ValidarEstaAtrasadoInicio(LocalDateTime expediente, long toleranciaMinutos) {
        long toleranciaEmMilisegundos = DateUtils.GetMilisegundosPorMinutos(toleranciaMinutos);
        long pontoAtual = DateUtils.GetMilisegundos(this.ponto);

        long toleranciaMinimo = DateUtils.GetMilisegundos(expediente, -toleranciaEmMilisegundos);
        long toleranciaMaximo = DateUtils.GetMilisegundos(expediente, toleranciaEmMilisegundos);
        long IniciDoDia = DateUtils.GetMilisegundos(DateUtils.IniciDesteDia());

        if (pontoAtual < IniciDoDia) {
            return EnumRelogioPontoEstado.Atrasado;
        } else if (pontoAtual < toleranciaMinimo) {
            return EnumRelogioPontoEstado.Adiantado;
        } else if (pontoAtual > toleranciaMaximo) {
            return EnumRelogioPontoEstado.Atrasado;
        }

        return EnumRelogioPontoEstado.NoHorario;
    }

    public EnumRelogioPontoEstado ValidarEstaAtrasadoFinal(LocalDateTime expediente, long toleranciaMinutos) {
        long toleranciaEmMilisegundos = DateUtils.GetMilisegundosPorMinutos(toleranciaMinutos);
        long pontoAtual = DateUtils.GetMilisegundos(this.ponto);

        long toleranciaMinimo = DateUtils.GetMilisegundos(expediente, -toleranciaEmMilisegundos);
        long toleranciaMaximo = DateUtils.GetMilisegundos(expediente, toleranciaEmMilisegundos);
        long IniciDoDia = DateUtils.GetMilisegundos(DateUtils.IniciDesteDia());

        if (pontoAtual < IniciDoDia) {
            return EnumRelogioPontoEstado.Atrasado;
        } else  if (pontoAtual < toleranciaMinimo) {
            return EnumRelogioPontoEstado.Adiantado;
        } else if (pontoAtual > toleranciaMaximo) {
            return EnumRelogioPontoEstado.Extra;
        }

        return EnumRelogioPontoEstado.NoHorario;
    }

	public boolean ValidarInconsistente(int quantidadePontosDia) {
        long pontoAtual = DateUtils.GetMilisegundos(this.ponto);
        long IniciDoDia = DateUtils.GetMilisegundos(DateUtils.IniciDesteDia());
        return pontoAtual > IniciDoDia && this.numeroPontoDia > quantidadePontosDia;
	}
}