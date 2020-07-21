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

    private final  IRelogioPontoRepository _repository;

    @Autowired
    public RelogioPontoService(IRelogioPontoRepository repository) {
        super(repository);
        _repository = repository;
    }

    public void ConfigurarEstaAtrasado(
            RelogioPonto entity,
            PreDefinicaoPonto preDefinicao) {
        int pontoNumeroDia = this.pontoNumeroDia(entity.getFuncionario().getId());

        EnumRelogioPontoEstado relogioPontoEstado;
        switch (pontoNumeroDia) {
            case 1:
                relogioPontoEstado = entity.ValidarEstaAtrasadoInicio(
                    preDefinicao.getInicioExpediente(),
                    preDefinicao.getMinutosTolerancia()
            );
                break;
            case 2:
                relogioPontoEstado = entity.ValidarEstaAtrasadoInicio(
                        preDefinicao.getInicioDescanso(),
                        preDefinicao.getMinutosTolerancia()
                );
                break;

            case 3:
                relogioPontoEstado = entity.ValidarEstaAtrasadoFinal(
                        preDefinicao.getFinalDescanso(),
                        preDefinicao.getMinutosTolerancia()
                );
               break;
            case 4:
                relogioPontoEstado = entity.ValidarEstaAtrasadoFinal(
                        preDefinicao.getFinalExpediente(),
                        preDefinicao.getMinutosTolerancia()
                );
                break;
            default:
                relogioPontoEstado = EnumRelogioPontoEstado.NoHorario;
                break;
        }

        entity.setRelogioPontoEstado(relogioPontoEstado);
    }

    public boolean ValidarLimiteAtraso(long funcionarioId, PreDefinicaoPonto preDefinicaoPonto) {
        long countPontoSemanaAtrasado = this._repository.countPontoSemanaAtrasado(
                funcionarioId, DateUtils.InicioDaSemana());
        long limite = preDefinicaoPonto.getLimiteQuantidadeAtrasoSemana();
        boolean atingiuLimite = countPontoSemanaAtrasado >= limite;
        return atingiuLimite;
    }

    public void setInconsistente(RelogioPonto entity, PreDefinicaoPonto preDefinicaoPonto) {
        boolean inconsistente = ValidarLimiteBatidas(entity.getFuncionario().getId(),
                preDefinicaoPonto.getLimiteBatidas());
        entity.setInconsistente(inconsistente);
    }

    public boolean ValidarLimiteBatidas(long funcionarioId, long limiteBatidas) {
        long countPontoHoje = countPontoHoje(funcionarioId);
        return countPontoHoje >= limiteBatidas;
    }

    public long countPontoHoje(long funcionarioId) {
        return this._repository.countPontoHoje(
                    funcionarioId
                    , DateUtils.IniciDoDia());
    }

    public int pontoNumeroDia(long funcionarioId) {
        return (int)countPontoHoje(funcionarioId) + 1;
    }

	public void configureSave(RelogioPonto entity, Funcionario funcionario) {
        entity.setFuncionario(funcionario);
        entity.setInconsistente(false);
        entity.setRelogioPontoEstado(EnumRelogioPontoEstado.NoHorario);
	}
}