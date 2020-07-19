package com.sistemapontoeletronico.controllers;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.services.biometria.BiometriaService;
import com.sistemapontoeletronico.domain.services.funcionario.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/biometria/")
public class BiometriaController {
    private final FuncionarioService _serviceFuncionario;
    private final BiometriaService _serviceBiometria;

    @Autowired
    public BiometriaController(
            final FuncionarioService serviceFuncionario,
            final  BiometriaService serviceBiometria) {
        _serviceFuncionario = serviceFuncionario;
        _serviceBiometria = serviceBiometria;
    }

    @GetMapping(path = "findById")
    public ResponseEntity<?> findById(@RequestParam(name = "id") long id) {
// Verificar se o Usuario que faz esse pedido é rh
        Funcionario entity = this._serviceFuncionario.findById(id);

        if (!Objects.nonNull(entity))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(entity);
    }

    @PostMapping(path = "save")
    public ResponseEntity<?> save(@RequestBody Funcionario entity) {
// Verificar se o Usuario que faz esse pedido é rh
        Funcionario newEntity = this._serviceFuncionario.save(entity);
        
        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(newEntity);
    }

    @PutMapping(path = "update")
    public ResponseEntity<?> update(@RequestBody Funcionario entity) {
// Verificar se o Usuario que faz esse pedido é rh
        Funcionario newEntity = this._serviceFuncionario.update(entity);
        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(newEntity);
    }

    @GetMapping(path = "deleteById")
    public ResponseEntity<?> deleteById(@RequestParam(name = "id") long id) {
// Verificar se o Usuario que faz esse pedido é rh
        boolean deleted = this._serviceFuncionario.deleteById(id);

        if (!deleted)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(deleted);
    }
}