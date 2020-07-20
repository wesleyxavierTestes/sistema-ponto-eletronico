package com.sistemapontoeletronico.controllers;

import java.util.List;
import java.util.Objects;

import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.services.funcionario.FuncionarioService;
import com.sistemapontoeletronico.domain.services.preDefinicaoPonto.PreDefinicaoPontoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predefinicaoponto/")
public class PreDefinicaoPontoController {
    private final FuncionarioService _serviceFuncionario;
    private final PreDefinicaoPontoService _servicePreDefinicaoPonto;

    @Autowired
    public PreDefinicaoPontoController(
            final FuncionarioService serviceFuncionario,
            final PreDefinicaoPontoService servicePreDefinicaoPonto) {
        _servicePreDefinicaoPonto = servicePreDefinicaoPonto;
        _serviceFuncionario = serviceFuncionario;
    }

    @GetMapping(path = "findAll")
    public ResponseEntity<?> findAll(
            @RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso
    ) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionarioId, acesso);
        if (!funcionarioAutorizado) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        // Só pode haver 1 única definição
        final Page<PreDefinicaoPonto> list = this._servicePreDefinicaoPonto.findAll(1);

        return ResponseEntity.ok(list);
    }

    @PostMapping(path = "save")
    public ResponseEntity<?> save(
            @RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso,
            @RequestBody PreDefinicaoPonto entity) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionarioId, acesso);
        if (!funcionarioAutorizado) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        final Page<PreDefinicaoPonto> list = this._servicePreDefinicaoPonto.findAll(1);
        if (Objects.nonNull(list) && list.getTotalElements() > 0) return new ResponseEntity<>
                (null, HttpStatus.LOCKED);

        PreDefinicaoPonto newEntity = this._servicePreDefinicaoPonto.save(entity);
        
        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(newEntity);
    }

    @PutMapping(path = "update")
    public ResponseEntity<?> update(
            @RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso,
            @RequestBody PreDefinicaoPonto entity) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionarioId, acesso);
        if (!funcionarioAutorizado) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        PreDefinicaoPonto newEntity = this._servicePreDefinicaoPonto.update(entity);
        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(newEntity);
    }

    @DeleteMapping(path = "deleteById")
    public ResponseEntity<?> deleteById(
            @RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso,
            @RequestParam(name = "id") long id) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionarioId, acesso);
        if (!funcionarioAutorizado) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        boolean deleted = this._servicePreDefinicaoPonto.deleteById(id);

        if (!deleted)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(deleted);
    }
}