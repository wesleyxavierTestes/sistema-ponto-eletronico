package com.sistemapontoeletronico.infra.repositorys;

import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IRelogioPontoRepository extends JpaRepository<RelogioPonto, Long>{
    @Query(
            nativeQuery = true,
            value = "select count(*) from public.relogio_ponto as u " +
                    "where u.funcionario_id = :funcionarioId " +
                    "where u.esta_atrasado = true " +
                    "and u.ponto > :InicioDaSemana " +
                    "and u.ponto < :FinalDaSemana ")
    long countPontoSemanaAtrasado(
            @Param("funcionarioId") long funcionarioId,
            @Param("InicioDaSemana") LocalDateTime InicioDaSemana,
            @Param("FinalDaSemana") LocalDateTime FinalDaSemana
    );

    @Query(
            nativeQuery = true,
            value = "select count(*) from public.relogio_ponto as u " +
                    "where u.funcionario_id = :funcionarioId " +
                    "and u.ponto > :IniciDoDia " +
                    "and u.ponto < :FinalDoDia ")
    long countPontoHoje(
            @Param("funcionarioId") long funcionarioId,
            @Param("IniciDoDia") LocalDateTime IniciDoDia,
            @Param("FinalDoDia") LocalDateTime FinalDoDia
    );
}