package com.sistemapontoeletronico.domain.services.funcionario;

import java.util.ArrayList;
import java.util.List;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.services.BaseService;
import com.sistemapontoeletronico.infra.repositorys.IFuncionarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService extends BaseService<Funcionario> {
    private final IFuncionarioRepository _repository;
    @Autowired
    public FuncionarioService(final IFuncionarioRepository repository) {
        super(repository);
        _repository = repository;
    }

    public boolean validaFuncionarioAutorizado(final long funcionarioId, final String acesso) {
        boolean exists = false;
        try {
            exists  = this._repository.existsByIdRh(funcionarioId, acesso) > 0;
        } catch (final Exception e) {
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

    public boolean validaFuncionarioPadraoAutorizado(final String acesso) {
        final boolean temAcesso = Funcionario.FuncionarioPadrao().getAcesso().equals(acesso);
        return temAcesso;
    }

    public void bloquearFuncionario(Funcionario funcionario, final boolean bloquear) {
        funcionario = this.findById(funcionario.getId());

        funcionario.setFuncionarioEstado(bloquear
        ? EnumFuncionarioEstado.Bloqueado : EnumFuncionarioEstado.Ativo);

        this.update(funcionario);
    }

	public List<Funcionario> findAllFuncionarioEstado(final EnumFuncionarioEstado funcionario_estado, int pagina) {
        List<Funcionario> lista = new ArrayList<>();

        lista = this._repository.findAllFuncionarioEstado(
            funcionario_estado.toString(),
            PageRequest.of((pagina - 1), 10)
            );

        // this._repository.findAll()
        // .stream()
        // .filter(c -> c.getFuncionarioEstado() == funcionario_estado)
        // .forEach(lista::add);
        
        return (List<Funcionario>)lista;
	}
}