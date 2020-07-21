package com.sistemapontoeletronico.controllers;

import java.util.Objects;

import com.sistemapontoeletronico.domain.entities.Acesso;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import com.sistemapontoeletronico.domain.enuns.EnumTipoOperacao;
import com.sistemapontoeletronico.domain.exceptions.AutorizationInitialException;
import com.sistemapontoeletronico.domain.services.funcionario.FuncionarioService;
import com.sistemapontoeletronico.domain.services.preDefinicaoPonto.PreDefinicaoPontoService;
import com.sistemapontoeletronico.domain.services.relogioPonto.RelogioPontoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relogioponto/")
public class RelogioPontoController {
    private final RelogioPontoService _serviceRelogioPonto;
    private final FuncionarioService _serviceFuncionario;
    private final PreDefinicaoPontoService _servicePreDefinicaoPonto;

    @Autowired
    public RelogioPontoController(final RelogioPontoService service, final FuncionarioService serviceFuncionario,
            final PreDefinicaoPontoService servicePreDefinicaoPonto) {
        _serviceRelogioPonto = service;
        _serviceFuncionario = serviceFuncionario;
        _servicePreDefinicaoPonto = servicePreDefinicaoPonto;
    }

    @GetMapping(path = "count")
    public ResponseEntity<?> count(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            long count = this._serviceRelogioPonto.count();
            return ResponseEntity.ok(count);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping(path = "findAll/{pagina}")
    public ResponseEntity<?> findAll(@PathVariable(name = "pagina") int pagina,
            @RequestHeader(name = "funcionarioId") long funcionarioId, @RequestParam(name = "acesso") String acesso) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            final Page<RelogioPonto> list = this._serviceRelogioPonto.findAll(pagina).map(c -> {
                c.setFuncionario(null);
                return c;
            });

            return ResponseEntity.ok(list);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PostMapping(path = "save/{tipoOperacao}")
    public ResponseEntity<?> save(@PathVariable(name = "tipoOperacao") EnumTipoOperacao tipoOperacao,
            @RequestBody @Validated Acesso acesso) {
        try {
            PreDefinicaoPonto preDefinicao = this._servicePreDefinicaoPonto.find();
            if (!Objects.nonNull(preDefinicao))
                return ResponseEntity.ok("Pré definição não definida");

            Funcionario funcionario = this._serviceFuncionario.findByCodigoAcesso(acesso.codigoAcesso, tipoOperacao);

            if (!Objects.nonNull(funcionario))
                return ResponseEntity.ok("Usuário sem Autorização");
            if (funcionario.EstaBloqueado())
                return ResponseEntity.ok("Usuário Bloqueado");

            RelogioPonto entity = new RelogioPonto(acesso.ponto, funcionario.getNome());

            this._serviceRelogioPonto.configureSave(entity, funcionario);
            this._serviceRelogioPonto.ConfigurarEstaAtrasadoInconsistencia(entity, preDefinicao);

            if (!Objects.nonNull(this._serviceRelogioPonto.save(entity)))
                return ResponseEntity.badRequest().build();

            boolean bloquear = this._serviceRelogioPonto.ValidarLimiteAtraso(funcionario.getId(), preDefinicao);

            if (bloquear) {
                this._serviceFuncionario.bloquearFuncionario(funcionario, bloquear);
                return ResponseEntity.ok("Usuário está bloqueado para novo registro, Procure o RH para desbloqueio");
            }

            return ResponseEntity.ok(entity);

        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @DeleteMapping(path = "deleteById")
    public ResponseEntity<?> deleteById(@RequestHeader(name = "funcionarioId") long funcionarioId,
            @RequestParam(name = "acesso") String acesso, @RequestParam(name = "id") long id) {
        try {
            boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            boolean deleted = this._serviceRelogioPonto.deleteById(id);

            if (!deleted)
                return ResponseEntity.badRequest().build();

            return ResponseEntity.ok(deleted);
        } catch (AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }
}