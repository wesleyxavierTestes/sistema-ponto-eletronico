package com.sistemapontoeletronico.controllers;

import java.util.List;
import java.util.Objects;

import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import com.sistemapontoeletronico.domain.services.preDefinicaoPonto.PreDefinicaoPontoService;
import com.sistemapontoeletronico.domain.services.relogioPonto.RelogioPontoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relogioponto/")
public class RelogioPontoController {
    private final RelogioPontoService _service;
    private final PreDefinicaoPontoService _servicePreDefinicaoPonto;

    @Autowired
    public RelogioPontoController(
            final RelogioPontoService service,
            PreDefinicaoPontoService servicePreDefinicaoPonto) {
        _service = service;
        _servicePreDefinicaoPonto = servicePreDefinicaoPonto;
    }

    @GetMapping(path = "count")
    public ResponseEntity<?> count() {
        long count = this._service.count();
        return ResponseEntity.ok(count);
    }

//    @GetMapping(path = "findAll")
//    public ResponseEntity<?> findAll() {
//        final List<RelogioPonto> list = this._service.findAll();
//        return ResponseEntity.ok(list);
//    }

//    @GetMapping(path = "findById")
//    public ResponseEntity<?> findById(@RequestParam(name = "id") long id) {
//
//        RelogioPonto entity = this._service.findById(id);
//
//        if (!Objects.nonNull(entity))
//            return ResponseEntity.badRequest().build();
//
//        return ResponseEntity.ok(entity);
//    }

    @PostMapping(path = "save")
    public ResponseEntity<?> save(@RequestBody RelogioPonto entity) {
        if (!Objects.nonNull(entity) || !Objects.nonNull(entity.getFuncionario()))
            return new ResponseEntity(null, HttpStatus.NOT_IMPLEMENTED);

        PreDefinicaoPonto preDefinicao = this._servicePreDefinicaoPonto.find();
        if (!Objects.nonNull(preDefinicao)) return ResponseEntity.notFound().build();

        this._service.ConfigurarEstaAtrasado(entity, preDefinicao);

        RelogioPonto newEntity = this._service.save(entity);
        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(newEntity);
    }

//    @PutMapping(path = "update")
//    public ResponseEntity<?> update(@RequestBody RelogioPonto entity) {
//
//        RelogioPonto newEntity = this._service.update(entity);
//        if (!Objects.nonNull(newEntity))
//            return ResponseEntity.badRequest().build();
//        return ResponseEntity.ok(newEntity);
//    }
//
//    @GetMapping(path = "deleteById")
//    public ResponseEntity<?> deleteById(@RequestParam(name = "id") long id) {
//
//        boolean deleted = this._service.deleteById(id);
//
//        if (!deleted)
//            return ResponseEntity.badRequest().build();
//
//        return ResponseEntity.ok(deleted);
//    }
}