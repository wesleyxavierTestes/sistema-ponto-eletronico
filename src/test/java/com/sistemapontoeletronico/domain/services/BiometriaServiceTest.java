package com.sistemapontoeletronico.domain.services;

import com.sistemapontoeletronico.domain.entities.biometria.Biometria;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioSetor;
import com.sistemapontoeletronico.domain.services.biometria.BiometriaService;
import com.sistemapontoeletronico.domain.services.funcionario.FuncionarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BiometriaServiceTest {
    private Funcionario funcionario;
    private Biometria biometria;

    @Autowired
    private BiometriaService _serviceBiometria;

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

        biometria = new Biometria();
        biometria.setCodigo("951753");
    }

    @Test
    void validacaoManualSelectNativo() {
        this._serviceFuncionario.save(funcionario);

        biometria.setFuncionario(funcionario);
        this._serviceBiometria.save(biometria);

       Long lista = this._serviceBiometria.findFuncionarioIdByBiometriaCodigo("951753");

        assertTrue(Objects.nonNull(lista));
    }

    @AfterEach
    void finallize() {
        if (Objects.nonNull(funcionario)) {
            this._serviceFuncionario.deleteById(funcionario.getId());
        }
    }
}
