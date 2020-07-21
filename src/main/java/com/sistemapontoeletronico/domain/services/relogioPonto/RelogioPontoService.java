package com.sistemapontoeletronico.domain.services.relogioPonto;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import com.sistemapontoeletronico.domain.enuns.EnumRelogioPontoEstado;
import com.sistemapontoeletronico.domain.services.BaseService;
import com.sistemapontoeletronico.infra.repositorys.IRelogioPontoRepository;

import com.sistemapontoeletronico.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelogioPontoService extends BaseService<RelogioPonto> {

    private final IRelogioPontoRepository _repository;

    @Autowired
    public RelogioPontoService(IRelogioPontoRepository repository) {
        super(repository);
        _repository = repository;
    }

    public void ConfigurarEstaAtrasadoInconsistencia(RelogioPonto entity, PreDefinicaoPonto preDefinicao) {
        entity.setNumeroPontoDia(this.pontoNumeroDia(entity.getFuncionario().getId()));

        boolean inconsistente = entity.ValidarInconsistente(preDefinicao.getQuantidadePontosDia());
        EnumRelogioPontoEstado relogioPontoEstado = EnumRelogioPontoEstado.Inconsistencia;

        if (!inconsistente) {
            switch (entity.getNumeroPontoDia()) {
                case 1:
                    relogioPontoEstado = entity.ValidarEstaAtrasadoInicio(
                            DateUtils.ConfigurarExpedienteDia(preDefinicao.getInicioExpediente()),
                            preDefinicao.getMinutosTolerancia());
                    break;
                case 2:
                    relogioPontoEstado = entity.ValidarEstaAtrasadoInicio(
                            DateUtils.ConfigurarExpedienteDia(preDefinicao.getInicioDescanso()),
                            preDefinicao.getMinutosTolerancia());
                    break;

                case 3:
                    relogioPontoEstado = entity.ValidarEstaAtrasadoInicio(
                            DateUtils.ConfigurarExpedienteDia(preDefinicao.getFinalDescanso()),
                            preDefinicao.getMinutosTolerancia());
                    break;
                case 4:
                    relogioPontoEstado = entity.ValidarEstaAtrasadoFinal(
                            DateUtils.ConfigurarExpedienteDia(preDefinicao.getFinalExpediente()),
                            preDefinicao.getMinutosTolerancia());
                    break;
                default:
                    relogioPontoEstado = EnumRelogioPontoEstado.NoHorario;
                    break;
            }
        }
        entity.setRelogioPontoEstado(relogioPontoEstado);
        entity.setInconsistente(inconsistente);
    }

    public boolean ValidarLimiteAtraso(long funcionarioId, PreDefinicaoPonto preDefinicaoPonto) {
        long countPontoSemanaAtrasado = this._repository.countPontoSemanaAtrasado(funcionarioId,
                DateUtils.InicioDaSemana());
        long limite = preDefinicaoPonto.getLimiteQuantidadeAtrasoSemana();
        boolean atingiuLimite = countPontoSemanaAtrasado >= limite;
        return atingiuLimite;
    }

    public long countPontoHoje(long funcionarioId) {
        return this._repository.countPontoHoje(funcionarioId, DateUtils.IniciDoDia());
    }

    public int pontoNumeroDia(long funcionarioId) {
        return (int) countPontoHoje(funcionarioId) + 1;
    }

    public void configureSave(RelogioPonto entity, Funcionario funcionario) {
        entity.setFuncionario(funcionario);
        entity.setInconsistente(false);
        entity.setRelogioPontoEstado(EnumRelogioPontoEstado.NoHorario);
    }
}