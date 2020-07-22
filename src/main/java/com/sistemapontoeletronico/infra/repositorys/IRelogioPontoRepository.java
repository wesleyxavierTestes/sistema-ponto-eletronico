package com.sistemapontoeletronico.infra.repositorys;

import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IRelogioPontoRepository extends JpaRepository<RelogioPonto, Long> {
        /**
         * Faz a contagem de todos os Relogios ponto que estõ atrasados na semana via
         * específico Funcionário
         * @param funcionarioId
         * @param InicioDaSemana
         * @return Contagem
         */
        @Query(nativeQuery = true, value = "select count(*) from public.relogio_ponto as u "
                        + "where u.funcionario_id = :funcionarioId " + "and u.relogio_ponto_estado = 'Atrasado' "
                        + "and u.ponto > :InicioDaSemana ")
        long countPontoSemanaAtrasado(@Param("funcionarioId") long funcionarioId,
                        @Param("InicioDaSemana") LocalDateTime InicioDaSemana);

        /**
         * Faz a contagem de todos os Relogios ponto realizados hoje via específico
         * Funcionário
         * @param funcionarioId
         * @param IniciDoDia
         * @return Contagem
         */
        @Query(nativeQuery = true, value = "select count(*) from public.relogio_ponto as u "
                        + "where u.funcionario_id = :funcionarioId " + "and u.ponto > :IniciDoDia ")
        long countPontoHoje(@Param("funcionarioId") long funcionarioId, @Param("IniciDoDia") LocalDateTime IniciDoDia);

        /**
         *  Busca Todos Relógios ponto com base em um intervalo de datas
         * via específico funcionário
         * @param iniciointervalo
         * @param finalintervalo
         * @param id
         * @return lista de relógios ponto
         */
        @Query(nativeQuery = true, value = "SELECT (f.*) " + "FROM public.relogio_ponto as f " + "where f.ponto > ?1 "
                        + "and f.ponto < ?2 " + "and f.funcionario_id = ?3 " + "ORDER BY ponto")
        List<RelogioPonto> relogioPontoIntervalo(LocalDateTime iniciointervalo, LocalDateTime finalintervalo, long id);

        /**
         * Busca Todos funcionários com base em um intervalo de datas
         * @param iniciointervalo
         * @param finalintervalo
         * @return Lista apenas dos Ids dos funcionários
         */
        @Query(nativeQuery = true, value = "SELECT distinct (f.funcionario_id) " + "FROM public.relogio_ponto as f "
                        + "where f.ponto > ?1 " + "and f.ponto < ?2 ")
        List<Long> relogioPontoIntervaloDistinctFuncionario(LocalDateTime iniciointervalo,
                        LocalDateTime finalintervalo);

        /***
         * Buscar nome do usuário via seu id
         * @param id 
         * @return Nome do Funcionário
         */
        @Query(nativeQuery = true, value = "SELECT (f.nome) " + "FROM public.funcionario as f " + "where f.id = ?1 ")
        String findNomeFuncionario(long id);
}