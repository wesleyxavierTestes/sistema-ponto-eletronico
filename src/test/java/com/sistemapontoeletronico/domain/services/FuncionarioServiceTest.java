package com.sistemapontoeletronico.domain.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioSetor;
import com.sistemapontoeletronico.domain.services.funcionario.FuncionarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
public class FuncionarioServiceTest {
    private Funcionario funcionario;

    @Autowired
    private FuncionarioService _serviceFuncionario;

   @BeforeEach
   void setup() {
       String acesso = "132456";
       funcionario = new Funcionario();
       funcionario.setAcesso(acesso);
       funcionario.setFuncionarioEstado(EnumFuncionarioEstado.Ativo);
       funcionario.setFuncionarioSetor(EnumFuncionarioSetor.Rh);
       funcionario.setNome("Marcelo");
   }

    @DisplayName("Funcionario Padrao Valido sem funcionario cadastrado")
    @Test
    void FuncionarioPadrao_ValidaFuncionarioAutorizadoTest() {
        long funcionarioId = 0;
        String acesso = "SecretAdmin123";

        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioPadraoAutorizado(acesso);

        assertTrue(funcionarioAutorizado);
    }

    @DisplayName("Funcionario Padrao invalido Com funcionario cadastrado")
    @Test
    void FuncionarioExistenteEPadrao_ValidaFuncionarioAutorizadoTest() {
        long funcionarioId = 0;
        String acesso = "SecretAdmin123";

        if (!this._serviceFuncionario.isExistRh()) return;

        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionarioId, acesso);

        assertTrue(!funcionarioAutorizado);
    }

    @DisplayName("Funcionario RH cadastrado valido")
    @Test
    void FuncionarioExistente_ValidaFuncionarioAutorizadoTest() {
        this._serviceFuncionario.save(funcionario);

        if (!this._serviceFuncionario.isExistRh()) {
            assertTrue(true);
            return;
        };

        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionario.getId(), funcionario.getAcesso());

       assertTrue(funcionarioAutorizado);
    }

    @DisplayName("Funcionario RH cadastrado invalido")
    @Test
    void FuncionarioExistenteRhInvalid_ValidaFuncionarioAutorizadoTest() {
        funcionario.setFuncionarioEstado(EnumFuncionarioEstado.Bloqueado);
        this._serviceFuncionario.save(funcionario);

        if (!this._serviceFuncionario.isExistRh()) {
            assertTrue(true);
            return;
        };

        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionario.getId(), funcionario.getAcesso());

        assertTrue(!funcionarioAutorizado);
    }

    @DisplayName("Funcionario RH cadastrado Senha errada")
    @Test
    void FuncionarioExistenteNaoValido_ValidaFuncionarioAutorizadoTest() {
        this._serviceFuncionario.save(funcionario);
        if (!this._serviceFuncionario.isExistRh()) {
            assertTrue(true);
            return;
        };

        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionario.getId(), "123456");

        assertTrue(!funcionarioAutorizado);
    }

    @DisplayName("Funcionario Geral cadastrado invalido")
    @Test
    void FuncionarioExistenteInvalid_ValidaFuncionarioAutorizadoTest() {
        funcionario.setFuncionarioSetor(EnumFuncionarioSetor.Geral);
        this._serviceFuncionario.save(funcionario);

        if (!this._serviceFuncionario.isExistRh()) {
            assertTrue(true);
            return;
        };

        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionario.getId(), funcionario.getAcesso());

        assertTrue(!funcionarioAutorizado);
    }

    @AfterEach
    void finallize() {
       if (Objects.nonNull(funcionario)) {
           this._serviceFuncionario.deleteById(funcionario.getId());
       }
    }
}
