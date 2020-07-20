package com.sistemapontoeletronico.domain.services.funcionario;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.services.BaseService;
import com.sistemapontoeletronico.infra.repositorys.IFuncionarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService extends BaseService<Funcionario> {
    private final IFuncionarioRepository _repository;
    @Autowired
    public FuncionarioService(IFuncionarioRepository repository) {
        super(repository);
        _repository = repository;
    }

    public boolean validaFuncionarioAutorizado(long funcionarioId, String acesso) {
        boolean exists = false;
        try {
            exists  = this._repository.existsByIdRh(funcionarioId, acesso) > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        if (!exists) {
            if (isExistRh()) return false;
            return validaFuncionarioPadraoAutorizado(acesso);
        }
        return exists;
    }

    public boolean isExistRh() {
        return this._repository.countRh() > 0;
    }

    public boolean validaFuncionarioPadraoAutorizado(String acesso) {
        boolean temAcesso = Funcionario.FuncionarioPadrao().getAcesso().equals(acesso);
        return temAcesso;
    }

    public void bloquearFuncionario(Funcionario funcionario, final boolean bloquear) {
        funcionario = this.findById(funcionario.getId());

        funcionario.setFuncionarioEstado(bloquear
        ? EnumFuncionarioEstado.Bloqueado : EnumFuncionarioEstado.Ativo);

        this.update(funcionario);
    }
}