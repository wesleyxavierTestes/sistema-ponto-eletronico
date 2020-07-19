package com.sistemapontoeletronico.controllers;

import java.util.List;
import java.util.Objects;

import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.services.preDefinicaoPonto.PreDefinicaoPontoService;

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
@RequestMapping("/api/rh/predefinicaoponto/")
public class PreDefinicaoPontoController {
    private final PreDefinicaoPontoService _service;

    @Autowired
    public PreDefinicaoPontoController(final PreDefinicaoPontoService service) {
        _service = service;
    }

    @GetMapping(path = "findAll")
    public ResponseEntity<?> findAll() {
// Verificar se o Usuario que faz esse pedido é rh
        // Só pode haver 1 única definição
        final List<PreDefinicaoPonto> list = this._service.findAll();

        return ResponseEntity.ok(list);
    }

    @PostMapping(path = "save")
    public ResponseEntity<?> save(@RequestBody PreDefinicaoPonto entity) {
        // Verificar se o Usuario que faz esse pedido é rh
// Só pode haver 1 única definição
        PreDefinicaoPonto newEntity = this._service.save(entity);
        
        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(newEntity);
    }

    @PutMapping(path = "update")
    public ResponseEntity<?> update(@RequestBody PreDefinicaoPonto entity) {
        // Verificar se o Usuario que faz esse pedido é rh
// Só pode haver 1 única definição
        PreDefinicaoPonto newEntity = this._service.update(entity);
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