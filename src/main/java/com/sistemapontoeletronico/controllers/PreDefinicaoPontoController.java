package com.sistemapontoeletronico.controllers;

import java.util.Objects;

import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.exceptions.AutorizationInitialException;
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
    public PreDefinicaoPontoController(final FuncionarioService serviceFuncionario,
            final PreDefinicaoPontoService servicePreDefinicaoPonto) {
        _servicePreDefinicaoPonto = servicePreDefinicaoPonto;
        _serviceFuncionario = serviceFuncionario;
    }

    @GetMapping(path = "findAll")
    public ResponseEntity<Object>findAll(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            // Só pode haver 1 única definição
            final Page<PreDefinicaoPonto> list = this._servicePreDefinicaoPonto.findAll(1);

            return ResponseEntity.ok(list);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PostMapping(path = "save")
    public ResponseEntity<Object>save(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestBody PreDefinicaoPonto entity) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            final Page<PreDefinicaoPonto> list = this._servicePreDefinicaoPonto.findAll(1);
            if (Objects.nonNull(list) && list.getTotalElements() > 0)
                return new ResponseEntity<>(null, HttpStatus.LOCKED);

            PreDefinicaoPonto newEntity = this._servicePreDefinicaoPonto.save(entity);

            if (!Objects.nonNull(newEntity))
                return ResponseEntity.badRequest().build();

            return ResponseEntity.ok(newEntity);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PutMapping(path = "update")
    public ResponseEntity<Object>update(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestBody PreDefinicaoPonto entity) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            PreDefinicaoPonto newEntity = this._servicePreDefinicaoPonto.update(entity);
            if (!Objects.nonNull(newEntity))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(newEntity);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @DeleteMapping(path = "deleteById")
    public ResponseEntity<Object>deleteById(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            boolean deleted = this._servicePreDefinicaoPonto.deleteById(id);

            if (!deleted)
                return ResponseEntity.badRequest().build();

            return ResponseEntity.ok(deleted);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }
}