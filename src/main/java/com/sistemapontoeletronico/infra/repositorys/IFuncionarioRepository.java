package com.sistemapontoeletronico.infra.repositorys;

import java.util.List;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IFuncionarioRepository extends JpaRepository<Funcionario, Long> {
        @Query(nativeQuery = true, value = "select count(*) from public.funcionario as u "
                        + "where u.funcionario_setor = 'Rh' " + "and u.funcionario_estado = 'Ativo' "
                        + "and u.id = :id " + "and u.acesso = :acesso " + " LIMIT 1")
        int existsByIdRh(@Param("id") long id, @Param("acesso") String acesso);

        @Query(nativeQuery = true, value = "select count(*) from public.funcionario as u "
                        + "where u.funcionario_setor = 'Rh' " + "and u.funcionario_estado = 'Ativo' ")
        long countRh();

        @Query(nativeQuery = true, value = "SELECT * FROM public.funcionario as f "
                        + "where f.funcionario_estado like :estado",
                        countQuery = "SELECT count(f.*) FROM public.funcionario as f "
                                        + "where f.funcionario_estado like :estado")
        List<Funcionario> findAllFuncionarioEstado(@Param("estado") String estado, Pageable pageable);
}