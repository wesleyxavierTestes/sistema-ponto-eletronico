package com.sistemapontoeletronico.controllers;

import java.util.List;
import java.util.Objects;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.services.biometria.BiometriaService;
import com.sistemapontoeletronico.domain.services.funcionario.FuncionarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/funcionario/")
public class FuncionarioController {
    private final FuncionarioService _service;
    @Autowired
    public FuncionarioController(
            final FuncionarioService service) {
        _service = service;
    }

    @GetMapping(path = "count")
    public ResponseEntity<?> count() {
// Verificar se o Usuario que faz esse pedido é rh
        final long count = this._service.count();

        return ResponseEntity.ok(count);
    }

    @GetMapping(path = "findAll")
    public ResponseEntity<?> findAll() {
// Verificar se o Usuario que faz esse pedido é rh
        final List<Funcionario> list = this._service.findAll();

        return ResponseEntity.ok(list);
    }

    @GetMapping(path = "findById")
    public ResponseEntity<?> findById(@RequestParam(name = "id") long id) {
// Verificar se o Usuario que faz esse pedido é rh
        Funcionario entity = this._service.findById(id);

        if (!Objects.nonNull(entity))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(entity);
    }

    @PostMapping(path = "save")
    public ResponseEntity<?> save(@RequestBody Funcionario entity) {
// Verificar se o Usuario que faz esse pedido é rh
        Funcionario newEntity = this._service.save(entity);
        
        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(newEntity);
    }

    @PutMapping(path = "update")
    public ResponseEntity<?> update(@RequestBody Funcionario entity) {
// Verificar se o Usuario que faz esse pedido é rh
        Funcionario newEntity = this._service.update(entity);
        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(newEntity);
    }

    @GetMapping(path = "deleteById")
    public ResponseEntity<?> deleteById(@RequestParam(name = "id") long id) {
// Verificar se o Usuario que faz esse pedido é rh
        boolean deleted = this._service.deleteById(id);

        if (!deleted)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(deleted);
    }
}