package com.sistemapontoeletronico.domain.services.relogioPonto;

import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import com.sistemapontoeletronico.domain.services.BaseService;
import com.sistemapontoeletronico.infra.repositorys.IRelogioPontoRepository;

import com.sistemapontoeletronico.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RelogioPontoService extends BaseService<RelogioPonto> {

    private final  IRelogioPontoRepository _repository;

    @Autowired
    public RelogioPontoService(IRelogioPontoRepository repository) {
        super(repository);
        _repository = repository;
    }

    public void ConfigurarEstaAtrasado(RelogioPonto entity, PreDefinicaoPonto preDefinicao) {
        boolean estaAtrasado = entity.ValidarEstaAtrasado(
                preDefinicao.getFinalExpediente(),
                preDefinicao.getMinutosTolerancia()
        );
        entity.setEstaAtrasado(estaAtrasado);
    }

    public boolean ValidarLimiteAtraso(RelogioPonto entity, PreDefinicaoPonto preDefinicaoPonto) {
        long countPontoSemanaAtrasado = this._repository.countPontoSemanaAtrasado(
                entity.getFuncionario().getId(), DateUtils.InicioDaSemana(), DateUtils.FinalDoDia());

        boolean atingiuLimite = countPontoSemanaAtrasado > preDefinicaoPonto.getLimiteQuantidadeAtrasoSemana();
        return atingiuLimite;
    }

    public void setInconsistente(RelogioPonto entity, PreDefinicaoPonto preDefinicaoPonto) {

        long countPontoHoje = this._repository.countPontoHoje(
                entity.getFuncionario().getId(), DateUtils.IniciDoDia(), DateUtils.FinalDoDia());

        boolean inconsistente =  countPontoHoje > preDefinicaoPonto.getLimiteBatidas();
        entity.setInconsistente(inconsistente);
    }
}