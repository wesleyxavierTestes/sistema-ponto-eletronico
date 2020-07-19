package com.sistemapontoeletronico.domain.services.relogioPonto;

import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import com.sistemapontoeletronico.domain.services.BaseService;
import com.sistemapontoeletronico.infra.repositorys.IRelogioPontoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelogioPontoService extends BaseService<RelogioPonto> {
    
    @Autowired
    public RelogioPontoService(IRelogioPontoRepository repository) {
        super(repository);
    }

    public void ConfigurarEstaAtrasado(RelogioPonto entity, PreDefinicaoPonto preDefinicao) {
        boolean estaAtrasda = entity.ValidarEstaAtrasado(
                preDefinicao.getFinalExpediente(),
                preDefinicao.getMinutosTolerancia()
        );
        entity.setEstaAtrasado(estaAtrasda);
    }
}