package com.sistemapontoeletronico.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.sistemapontoeletronico.domain.dto.AcessoDto;
import com.sistemapontoeletronico.domain.dto.HoraDiaDto;
import com.sistemapontoeletronico.domain.dto.ResponseEntityDto;
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
    public ResponseEntity<Object>count(@RequestHeader(name = "funcionarioId") final long funcionarioId,
            @RequestParam(name = "acesso") final String acesso) {
        try {
            final boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            final long count = this._serviceRelogioPonto.count();
            return ResponseEntity.ok(count);
        } catch (final AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping(path = "findAll/horas")
    public ResponseEntity<Object>findAllHoras(
            @RequestHeader(name = "funcionarioId") final long funcionarioId, 
            @RequestParam(name = "acesso") final String acesso,
            @RequestParam(name = "intervaloinicio") final String intervaloinicio,
            @RequestParam(name = "intervalofinal") final String intervalofinal
            ) {
        try {
            final boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            final List<HoraDiaDto> lista = this._serviceRelogioPonto
            .relogioPontoIntervalo(
                LocalDateTime.parse(intervaloinicio), 
               LocalDateTime.parse(intervalofinal));

            return ResponseEntity.ok(lista);
        } catch (final AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping(path = "findAll/{pagina}")
    public ResponseEntity<Object>findAll(@PathVariable(name = "pagina") final int pagina,
            @RequestHeader(name = "funcionarioId") final long funcionarioId, @RequestParam(name = "acesso") final String acesso) {
        try {
            final boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            final Page<RelogioPonto> list = this._serviceRelogioPonto.findAll(pagina).map(c -> {
                c.setFuncionario(null);
                return c;
            });

            return ResponseEntity.ok(list);
        } catch (final AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    /**
     * @apiNote 
     * @param tipoOperacao
     * @param acesso
     * @return
     */
    @PostMapping(path = "save/{tipoOperacao}")
    public ResponseEntity<ResponseEntityDto> save(@PathVariable(name = "tipoOperacao") final EnumTipoOperacao tipoOperacao,
            @RequestBody @Validated final AcessoDto acesso) {
        try {
            final PreDefinicaoPonto preDefinicao = this._servicePreDefinicaoPonto.find();
            if (!Objects.nonNull(preDefinicao))
                return ResponseEntity.ok(ResponseEntityDto
                .builder()
                .valido(false)
                .erro("Pré definição não definida")
                .build());

            final Funcionario funcionario = this._serviceFuncionario.findByCodigoAcesso(acesso.codigoAcesso, tipoOperacao);

            if (!Objects.nonNull(funcionario))
                return ResponseEntity.ok(ResponseEntityDto
                .builder()
                .erro("Usuário sem Autorização")
                .build());
            if (funcionario.validaEstaBloqueado())
                return ResponseEntity.ok(ResponseEntityDto
                .builder()
                .erro("Usuário Bloqueado")
                .build());

            final RelogioPonto entity = new RelogioPonto(acesso.ponto, funcionario.getNome());

            this._serviceRelogioPonto.configureSave(entity, funcionario);
            this._serviceRelogioPonto.ConfigurarEstaAtrasadoInconsistencia(entity, preDefinicao);

            if (!Objects.nonNull(this._serviceRelogioPonto.save(entity)))
                return ResponseEntity.badRequest().build();

            final boolean bloquear = this._serviceRelogioPonto.ValidarLimiteAtraso(funcionario.getId(), preDefinicao);

            if (bloquear) {
                this._serviceFuncionario.bloquearFuncionario(funcionario, bloquear);
                return ResponseEntity.ok(ResponseEntityDto
                .builder()
                .erro("Usuário está bloqueado para novo registro, Procure o RH para desbloqueio")
                .build());
            }

            return ResponseEntity.ok(ResponseEntityDto
            .builder()
            .valido(true)
            .objeto(entity)
            .build());

        } catch (final Exception e) {
            return ResponseEntity.ok(ResponseEntityDto
            .builder()
            .erro(e.getMessage())
            .build());
        }
    }

    /**
     * 
     * @param funcionarioId
     * @param acesso
     * @param id
     * @return
     */
    @DeleteMapping(path = "deleteById")
    public ResponseEntity<Object>deleteById(@RequestHeader(name = "funcionarioId") final long funcionarioId,
            @RequestParam(name = "acesso") final String acesso, @RequestParam(name = "id") final long id) {
        try {
            final boolean funcionarioAutorizado = this._serviceFuncionario.validaFuncionarioAutorizado(funcionarioId, acesso);
            if (!funcionarioAutorizado)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            final boolean deleted = this._serviceRelogioPonto.deleteById(id);

            if (!deleted)
                return ResponseEntity.badRequest().build();

            return ResponseEntity.ok(deleted);
        } catch (final AutorizationInitialException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }
}