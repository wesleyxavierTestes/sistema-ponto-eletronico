package com.sistemapontoeletronico.domain.services.biometria;

import com.sistemapontoeletronico.domain.entities.biometria.Biometria;
import com.sistemapontoeletronico.domain.services.BaseService;
import com.sistemapontoeletronico.infra.repositorys.IBiometriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BiometriaService extends BaseService<Biometria> {
    private final IBiometriaRepository _repository;
    @Autowired
    public BiometriaService(IBiometriaRepository repository) {
        super(repository);
        _repository = repository;
    }

    /**
     * 
     * @param codigoBiometia da biometria
     * @return Tipo Long de Contagem
     */
    public Long findFuncionarioIdByBiometriaCodigo(String codigoBiometia) {
        return this._repository.findFuncionarioIdByBiometriaCodigo(codigoBiometia);
    }
}