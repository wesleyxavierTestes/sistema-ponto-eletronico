package com.sistemapontoeletronico.controllers;

import java.util.List;
import java.util.Objects;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.services.funcionario.FuncionarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/funcionario/")
public class FuncionarioController {
    private final FuncionarioService _serviceFuncionario;

    @Autowired
    public FuncionarioController(final FuncionarioService serviceFuncionario) {
        _serviceFuncionario = serviceFuncionario;
    }

    @GetMapping(path = "count")
    public ResponseEntity<?> count(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        final long count = this._serviceFuncionario.count();

        return ResponseEntity.ok(count);
    }

    @GetMapping(path = "findAll/{pagina}")
    public ResponseEntity<?> findAll(@PathVariable(name = "pagina") int pagina,
            @RequestHeader(name = "funcionarioId") long funcionarioId, @RequestParam(name = "acesso") String acesso) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        final Page<Funcionario> list = this._serviceFuncionario.findAll(pagina);

        return ResponseEntity.ok(list);
    }

    @GetMapping(path = "findAll/bloqueado/{pagina}")
    public ResponseEntity<?> findAllbloqueado(
        @PathVariable(name = "pagina") int pagina,
            @RequestHeader(name = "funcionarioId") long funcionarioId, @RequestParam(name = "acesso") String acesso) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        final List<Funcionario> list = this._serviceFuncionario
        .findAllFuncionarioEstado(EnumFuncionarioEstado.Bloqueado, pagina);

        return ResponseEntity.ok(list);
    }

    @GetMapping(path = "findById")
    public ResponseEntity<?> findById(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Funcionario entity = this._serviceFuncionario.findById(id);

        if (!Objects.nonNull(entity))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(entity);
    }

    @PostMapping(path = "save")
    public ResponseEntity<?> save(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestBody Funcionario entity) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Funcionario newEntity = this._serviceFuncionario.save(entity);

        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(newEntity);
    }

    @PutMapping(path = "update")
    public ResponseEntity<?> update(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestBody Funcionario entity) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Funcionario newEntity = this._serviceFuncionario.update(entity);
        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(newEntity);
    }

    @DeleteMapping(path = "deleteById")
    public ResponseEntity<?> deleteById(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {

        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        boolean deleted = this._serviceFuncionario.deleteById(id);

        if (!deleted)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(deleted);
    }

    @GetMapping(path = "desbloquear")
    public ResponseEntity<?> desbloquear(
        @RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Funcionario entity = this._serviceFuncionario.findById(id);

        if (!Objects.nonNull(entity))
            return ResponseEntity.ok("Usuário inexistente");
        if (entity.getId() == funcionarioId)
            return ResponseEntity.ok("Usuário não pode executar esta ação");
        if (!entity.EstaBloqueado())
            return ResponseEntity.ok("Usuário não está bloqueado");

        this._serviceFuncionario.bloquearFuncionario(entity, false);

        return ResponseEntity.ok(entity);
    }

    @GetMapping(path = "bloquear")
    public ResponseEntity<?> bloquear(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Funcionario entity = this._serviceFuncionario.findById(id);

        if (!Objects.nonNull(entity))
            return ResponseEntity.ok("Usuário inexistente");
        if (entity.getId() == funcionarioId)
            return ResponseEntity.ok("Usuário não pode executar esta ação");
        if (entity.EstaBloqueado())
            return ResponseEntity.ok("Usuário não está ativo");

        this._serviceFuncionario.bloquearFuncionario(entity, true);

        return ResponseEntity.ok(entity);
    }
}