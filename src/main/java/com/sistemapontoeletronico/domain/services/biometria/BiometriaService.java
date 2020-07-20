package com.sistemapontoeletronico.domain.services.biometria;

import com.sistemapontoeletronico.domain.entities.biometria.Biometria;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.services.BaseService;
import com.sistemapontoeletronico.infra.repositorys.IBiometriaRepository;
import com.sistemapontoeletronico.infra.repositorys.IFuncionarioRepository;
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

    public Long findFuncionarioIdByBiometriaCodigo(String codigo) {
        return this._repository.findFuncionarioIdByBiometriaCodigo(codigo);
    }
}