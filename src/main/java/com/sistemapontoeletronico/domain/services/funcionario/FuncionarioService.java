package com.sistemapontoeletronico.domain.services.funcionario;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.enuns.EnumTipoOperacao;
import com.sistemapontoeletronico.domain.exceptions.AutorizationInitialException;
import com.sistemapontoeletronico.domain.services.BaseService;
import com.sistemapontoeletronico.infra.repositorys.IFuncionarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService extends BaseService<Funcionario> {
    private final IFuncionarioRepository _repository;
    @Autowired
    public FuncionarioService(final IFuncionarioRepository repository) {
        super(repository);
        _repository = repository;
    }

    public boolean validaFuncionarioAutorizado
    (final long funcionarioId, final String acesso) throws AutorizationInitialException {
        boolean exists = false;
        try {
            exists  = this._repository.existsByIdRh(funcionarioId, acesso) > 0;
        } catch (final Exception e) {
            System.out.println(e);
        }

        if (!exists && this.count() == 0) {
            throw new AutorizationInitialException("Sistema sem Usuários com permissão.");
        }
        return exists;
    }

    public boolean validaFuncionarioAutorizadoPadrao(final String nome, final String acesso)throws AutorizationInitialException{        
        if (isExistRh()) throw new AutorizationInitialException("");
        return validaFuncionarioPadraoAutorizado(nome, acesso);
    }

    public boolean isExistRh() {
        return this._repository.countRh() > 0;
    }

    public boolean validaFuncionarioPadraoAutorizado(final String nome, final String acesso) {
        final boolean acessoOk = Funcionario.funcionarioPadrao().getAcesso().equals(acesso);
        final boolean nomeOk = Funcionario.funcionarioPadrao().getNome().equals(nome);
        return acessoOk && nomeOk;
    }

    public void bloquearFuncionario(Funcionario funcionario, final boolean bloquear) {
        funcionario = this.findById(funcionario.getId());

        funcionario.setFuncionarioEstado(bloquear
        ? EnumFuncionarioEstado.Bloqueado : EnumFuncionarioEstado.Ativo);

        this.update(funcionario);
    }

	public Page<Funcionario> findAllFuncionarioEstado(
        final EnumFuncionarioEstado funcionario_estado, int pagina) {

        Page<Funcionario> lista = this._repository.findAllFuncionarioEstado(
            funcionario_estado.toString(), PageRequest.of((pagina - 1), 10, Sort.by("id"))
            );        
        return lista;
	}

	public Funcionario findByCodigoAcesso(String codigoAcesso, EnumTipoOperacao tipoOperacao) {
        if (codigoAcesso == null || codigoAcesso.isEmpty()) return null;

        Funcionario funcionario = tipoOperacao == EnumTipoOperacao.biometria
        ? this._repository.findByCodigoAcessoBiometria(codigoAcesso)
        : this._repository.findByCodigoAcessoManual(codigoAcesso);

        return funcionario;
	}

	public Page<Funcionario> findAllFuncionarioBiometriaFaltante(int pagina) {
        Page<Funcionario> funcionarios =  
        this._repository.findAllFuncionarioBiometriaFaltante(
            PageRequest.of((pagina - 1), 10, Sort.by("id")));
        return funcionarios;
    }
}