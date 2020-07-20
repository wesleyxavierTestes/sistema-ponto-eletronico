package com.sistemapontoeletronico.controllers;

import java.util.Objects;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import com.sistemapontoeletronico.domain.services.funcionario.FuncionarioService;
import com.sistemapontoeletronico.domain.services.preDefinicaoPonto.PreDefinicaoPontoService;
import com.sistemapontoeletronico.domain.services.relogioPonto.RelogioPontoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relogioponto/")
public class RelogioPontoController {
    private final RelogioPontoService _serviceRelogioPonto;
    private final FuncionarioService _serviceFuncionario;
    private final PreDefinicaoPontoService _servicePreDefinicaoPonto;

    @Autowired
    public RelogioPontoController(final RelogioPontoService service, FuncionarioService serviceFuncionario,
            final PreDefinicaoPontoService servicePreDefinicaoPonto) {
        _serviceRelogioPonto = service;
        _serviceFuncionario = serviceFuncionario;
        _servicePreDefinicaoPonto = servicePreDefinicaoPonto;
    }

    @GetMapping(path = "count")
    public ResponseEntity<?> count() {
        long count = this._serviceRelogioPonto.count();
        return ResponseEntity.ok(count);
    }

    @GetMapping(path = "findAll/{pagina}")
    public ResponseEntity<?> findAll(@PathVariable(name = "pagina") int pagina,
            @RequestHeader(name = "funcionarioId") long funcionarioId, @RequestParam(name = "acesso") String acesso) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        final Page<RelogioPonto> list = this._serviceRelogioPonto.findAll(pagina)
                                    .map(c -> {
                                        c.setFuncionario(null);
                                        return c;
                                    });
        
        return ResponseEntity.ok(list);
    }

    @PostMapping(path = "save")
    public ResponseEntity<?> save(@RequestBody RelogioPonto entity) {
        if (!Objects.nonNull(entity) || !Objects.nonNull(entity.getPonto())
                || !Objects.nonNull(entity.getFuncionario()))
            return new ResponseEntity(null, HttpStatus.NOT_IMPLEMENTED);

        Funcionario funcionario = this._serviceFuncionario.findById(entity.getFuncionario().getId());
        if (!Objects.nonNull(funcionario) || funcionario.EstaBloqueado())
            return ResponseEntity.ok("Usuário Bloqueado");

        PreDefinicaoPonto preDefinicao = this._servicePreDefinicaoPonto.find();
        if (!Objects.nonNull(preDefinicao))
            return ResponseEntity.notFound().build();
        this._serviceRelogioPonto.ConfigurarEstaAtrasado(entity, preDefinicao);
        this._serviceRelogioPonto.setInconsistente(entity, preDefinicao);

        RelogioPonto newEntity = this._serviceRelogioPonto.save(entity);

        if (!Objects.nonNull(newEntity))
            return ResponseEntity.badRequest().build();

        boolean bloquear = this._serviceRelogioPonto.ValidarLimiteAtraso(entity.getFuncionario().getId(), preDefinicao);

        if (bloquear) {
            this._serviceFuncionario.bloquearFuncionario(entity.getFuncionario(), bloquear);
            return ResponseEntity.ok("Usuário está bloqueado para novo registro, Procure o RH para desbloqueio");
        }

        return ResponseEntity.ok(newEntity);
    }

    @DeleteMapping(path = "deleteById")
    public ResponseEntity<?> deleteById(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {
        boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
        if (!funcionarioAutorizado)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        boolean deleted = this._serviceRelogioPonto.deleteById(id);

        if (!deleted)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(deleted);
    }
}