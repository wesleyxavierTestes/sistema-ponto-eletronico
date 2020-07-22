package com.sistemapontoeletronico.domain.services.relogioPonto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.sistemapontoeletronico.domain.dto.HoraDiaDto;
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

    /**
     * @param entity
     * @param preDefinicao
     */
    public void ConfigurarEstaAtrasadoInconsistencia(RelogioPonto entity, PreDefinicaoPonto preDefinicao) {
        entity.setNumeroPontoDia(this.pontoNumeroDia(entity.getFuncionario().getId()));

        boolean inconsistente = entity.ValidarInconsistente(preDefinicao.getQuantidadePontosDia());
        EnumRelogioPontoEstado relogioPontoEstado = EnumRelogioPontoEstado.Inconsistencia;

        if (!inconsistente) {
            switch (entity.getNumeroPontoDia()) {
                case 1:
                    relogioPontoEstado = entity.validarEstaAtrasadoInicio(
                            DateUtils.ConfigurarExpedienteDia(preDefinicao.getInicioExpediente()),
                            preDefinicao.getMinutosTolerancia());
                    break;
                case 2:
                    relogioPontoEstado = entity.validarEstaAtrasadoInicio(
                            DateUtils.ConfigurarExpedienteDia(preDefinicao.getInicioDescanso()),
                            preDefinicao.getMinutosTolerancia());
                    break;

                case 3:
                    relogioPontoEstado = entity.validarEstaAtrasadoInicio(
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

    /**
     * @param funcionarioId
     * @param preDefinicaoPonto
     * @return
     */
    public boolean ValidarLimiteAtraso(long funcionarioId, PreDefinicaoPonto preDefinicaoPonto) {
        long countPontoSemanaAtrasado = countPontoSemanaAtrasado(funcionarioId);
        long limite = preDefinicaoPonto.getLimiteQuantidadeAtrasoSemana();
        boolean atingiuLimite = countPontoSemanaAtrasado >= limite;
        return atingiuLimite;
    }

    public long countPontoSemanaAtrasado(long funcionarioId) {
        return this._repository.countPontoSemanaAtrasado(funcionarioId, DateUtils.InicioDestaSemana());
    }

    public long countPontoHoje(long funcionarioId) {
        return this._repository.countPontoHoje(funcionarioId, DateUtils.IniciDesteDia());
    }

    public int pontoNumeroDia(long funcionarioId) {
        return (int) countPontoHoje(funcionarioId) + 1;
    }

    public void configureSave(RelogioPonto entity, Funcionario funcionario) {
        entity.setFuncionario(funcionario);
        entity.setInconsistente(false);
        entity.setRelogioPontoEstado(EnumRelogioPontoEstado.NoHorario);
    }

    public List<HoraDiaDto> relogioPontoIntervalo(
        LocalDateTime inicioIntervalo, LocalDateTime finalIntervalo) {
        inicioIntervalo = DateUtils.ZerarHoras(inicioIntervalo);
        List<HoraDiaDto> listaDiaDtos = new ArrayList<>();

        List<Long> idSFuncionario = this._repository.relogioPontoIntervaloDistinctFuncionario(inicioIntervalo,
                finalIntervalo);

        int totalDias = finalIntervalo.getDayOfMonth() - inicioIntervalo.getDayOfMonth();
        // for (long idFuncionario : idSFuncionario) {
        //     for (int i = 0; i <= totalDias; i++) {
                
        //         List<RelogioPonto> pontos = this._repository.relogioPontoIntervalo(
        //                 DateUtils.AddDays(inicioIntervalo, 0), 
        //                 DateUtils.AddDays(inicioIntervalo, 1), 
        //                 idFuncionario);

        //         String nomeFuncionario = this._repository.findNomeFuncionario(idFuncionario);

        //         List<LocalDateTime> todosPontos = new ArrayList<>();
        //         pontos.stream().map(c-> c.getPonto()).forEach(todosPontos::add);

        //         if (pontos.size() > 2) {
        //             RelogioPonto entradaExpediente = pontos.get(0);
        //             RelogioPonto saidaExpediente = pontos.get(pontos.size() - 1);

        //             //// LOGICA PARA CALCULOS DE TOTAL HORAS
        //             LocalTime horas = DateUtils.ContarTotalHoras(entradaExpediente, saidaExpediente);
        //             listaDiaDtos.add(HoraDiaDto
        //                             .builder()
        //                             .nome(nomeFuncionario)
        //                             .pontos(todosPontos)
        //                             .totalHoras(horas)
        //                             .build());

        //             pontos.remove(entradaExpediente);
        //             pontos.remove(saidaExpediente);
        //         }

        //         if (!pontos.isEmpty()) {
        //             if (pontos.size() % 2 != 0) {
        //                 // LÓGICA PARA ADD QUANDO NÃO HOUVER MAIS PAR DE PONTOS VÁLIDOS
        //                 listaDiaDtos.add(new HoraDiaDto());
        //             } else {
        //                 // LÓGICA PARA ADD QUANDO HOUVER MAIS PAR DE PONTOS VÁLIDOS

        //                 for (int j = 0; j < pontos.size(); j = j+2) {
        //                     RelogioPonto entrada = pontos.get(j);
        //                     RelogioPonto saida = pontos.get(j + 1);

        //                     //// LOGICA PARA CALCULOS DE TOTAL HORAS
        //                     LocalTime horas = DateUtils.ContarTotalHoras(entrada, saida);
        //                     listaDiaDtos.add(HoraDiaDto
        //                                     .builder()
        //                                     .nome(nomeFuncionario)
        //                                     .pontos(todosPontos)
        //                                     .totalHoras(horas)
        //                                     .build());
        //                 }
        //             }
        //         }
        //     }
        // }

        return listaDiaDtos;
    }
}