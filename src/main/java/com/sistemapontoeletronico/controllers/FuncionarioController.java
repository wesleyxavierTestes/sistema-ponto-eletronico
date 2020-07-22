package com.sistemapontoeletronico.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.sistemapontoeletronico.domain.dto.HoraDiaDto;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioEstado;
import com.sistemapontoeletronico.domain.enuns.EnumFuncionarioSetor;
import com.sistemapontoeletronico.domain.exceptions.AutorizationInitialException;
import com.sistemapontoeletronico.domain.services.funcionario.FuncionarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<Object>count(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            final long count = this._serviceFuncionario.count();

            return ResponseEntity.ok(count);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping(path = "findAll/{pagina}")
    public ResponseEntity<Object>findAll(@PathVariable(name = "pagina") int pagina,
            @RequestHeader(name = "funcionarioId") long funcionarioId, @RequestParam(name = "acesso") String acesso) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            final Page<Funcionario> list = this._serviceFuncionario.findAll(pagina);

            return ResponseEntity.ok(list);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping(path = "findAll/biometria/faltante/{pagina}")
    public ResponseEntity<Object>findAllbiometria(@PathVariable(name = "pagina") int pagina,
            @RequestHeader(name = "funcionarioId") long funcionarioId, @RequestParam(name = "acesso") String acesso) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            final Page<Funcionario> list = this._serviceFuncionario
                    .findAllFuncionarioBiometriaFaltante(pagina);

            return ResponseEntity.ok(list);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping(path = "findAll/bloqueado/{pagina}")
    public ResponseEntity<Object>findAllbloqueado(@PathVariable(name = "pagina") int pagina,
            @RequestHeader(name = "funcionarioId") long funcionarioId, @RequestParam(name = "acesso") String acesso) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            final Page<Funcionario> list = this._serviceFuncionario
                    .findAllFuncionarioEstado(EnumFuncionarioEstado.Bloqueado, pagina);

            return ResponseEntity.ok(list);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping(path = "findById")
    public ResponseEntity<Object>findById(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Funcionario entity = this._serviceFuncionario.findById(id);

            if (!Objects.nonNull(entity))
                return ResponseEntity.badRequest().build();

            return ResponseEntity.ok(entity);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PostMapping(path = "save/inicial")
    public ResponseEntity<Object>saveinicial(@RequestParam(name = "funcionario") String funcionario,
            @RequestParam(name = "acesso") String acesso, @RequestBody @Validated Funcionario entity) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizadoPadrao(funcionario, acesso);

            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            entity.setFuncionarioEstado(EnumFuncionarioEstado.Ativo);
            entity.setFuncionarioSetor(EnumFuncionarioSetor.Rh);

            Funcionario newEntity = this._serviceFuncionario.save(entity);

            if (!Objects.nonNull(newEntity))
                return ResponseEntity.badRequest().build();

            return ResponseEntity.ok(newEntity);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "save")
    public ResponseEntity<Object>save(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestBody @Validated Funcionario entity) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);

            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Funcionario newEntity = this._serviceFuncionario.save(entity);

            if (!Objects.nonNull(newEntity))
                return ResponseEntity.badRequest().build();

            return ResponseEntity.ok(newEntity);
        } catch (AutorizationInitialException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch(DataIntegrityViolationException e) {
            return ResponseEntity.ok("Código de Acesso não Permitido");
        }
    }

    @PutMapping(path = "update")
    public ResponseEntity<Object>update(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestBody @Validated Funcionario entity) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Funcionario newEntity = this._serviceFuncionario.update(entity);
            if (!Objects.nonNull(newEntity))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(newEntity);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        } catch(DataIntegrityViolationException e) {
            return ResponseEntity.ok("Código de Acesso não Permitido");
        }
    }

    @DeleteMapping(path = "deleteById")
    public ResponseEntity<Object>deleteById(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            boolean deleted = this._serviceFuncionario.deleteById(id);

            if (!deleted)
                return ResponseEntity.badRequest().build();

            return ResponseEntity.ok(deleted);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping(path = "desbloquear")
    public ResponseEntity<Object>desbloquear(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Funcionario entity = this._serviceFuncionario.findById(id);

            if (!Objects.nonNull(entity))
                return ResponseEntity.ok("Usuário inexistente");
            if (entity.getId() == funcionarioId)
                return ResponseEntity.ok("Usuário não pode executar esta ação");
            if (!entity.validaEstaBloqueado())
                return ResponseEntity.ok("Usuário não está bloqueado");

            this._serviceFuncionario.bloquearFuncionario(entity, false);

            return ResponseEntity.ok(entity);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping(path = "bloquear")
    public ResponseEntity<Object>bloquear(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Funcionario entity = this._serviceFuncionario.findById(id);

            if (!Objects.nonNull(entity))
                return ResponseEntity.ok("Usuário inexistente");
            if (entity.getId() == funcionarioId)
                return ResponseEntity.ok("Usuário não pode executar esta ação");
            if (entity.validaEstaBloqueado())
                return ResponseEntity.ok("Usuário não está ativo");

            this._serviceFuncionario.bloquearFuncionario(entity, true);

            return ResponseEntity.ok(entity);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }
}