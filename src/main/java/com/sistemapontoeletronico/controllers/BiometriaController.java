package com.sistemapontoeletronico.controllers;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.services.biometria.BiometriaService;
import com.sistemapontoeletronico.domain.services.funcionario.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping(path = "count")
    public ResponseEntity<?> count(
            @RequestParam(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso
    ) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionarioId, acesso);
        if (!funcionarioAutorizado) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        long entity = this._serviceFuncionario.count();
        return ResponseEntity.ok(entity);
    }

    @PostMapping(path = "save")
    public ResponseEntity<?> save(
            @RequestParam(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso,
            @RequestBody Funcionario entity) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(
                funcionarioId, acesso);
        if (!funcionarioAutorizado) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

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

    @DeleteMapping(path = "deleteById")
    public ResponseEntity<?> deleteById(@RequestParam(name = "id") long id) {
        boolean deleted = this._serviceFuncionario.deleteById(id);

        if (!deleted)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(deleted);
    }
}