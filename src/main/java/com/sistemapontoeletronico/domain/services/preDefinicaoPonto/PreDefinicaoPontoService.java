package com.sistemapontoeletronico.domain.services.preDefinicaoPonto;

import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.services.BaseService;
import com.sistemapontoeletronico.infra.repositorys.IPreDefinicaoPontoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PreDefinicaoPontoService extends BaseService<PreDefinicaoPonto> {

    private IPreDefinicaoPontoRepository _repository;
    @Autowired
    public PreDefinicaoPontoService(IPreDefinicaoPontoRepository repository) {
        super(repository);
        _repository = repository;
    }

    public PreDefinicaoPonto find() {
        List<PreDefinicaoPonto> listaEntity = this._repository.findAll();
        if (Objects.nonNull(listaEntity)) listaEntity.get(0);
        return null;
    }
}