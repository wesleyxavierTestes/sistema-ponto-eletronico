package com.sistemapontoeletronico.domain.services.funcionario;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.services.BaseService;
import com.sistemapontoeletronico.infra.repositorys.IFuncionarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService extends BaseService<Funcionario> {
    
    @Autowired
    public FuncionarioService(IFuncionarioRepository repository) {
        super(repository);
    }
}