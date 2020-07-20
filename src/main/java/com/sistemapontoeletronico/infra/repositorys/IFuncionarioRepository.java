package com.sistemapontoeletronico.infra.repositorys;

import com.sistemapontoeletronico.domain.entities.biometria.Biometria;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFuncionarioRepository extends JpaRepository<Funcionario, Long>{
    @Query(
            nativeQuery = true,
            value = "select count(*) from public.funcionario as u " +
                    "where u.funcionario_setor = 'Rh' " +
                    "and u.funcionario_estado = 'Ativo' " +
                    "and u.id = :id " +
                    "and u.acesso = :acesso " +
                    " LIMIT 1")
    int existsByIdRh(@Param("id") long id, @Param("acesso") String acesso);

    @Query(
    nativeQuery = true,
    value = "select count(*) from public.funcionario as u " +
            "where u.funcionario_setor = 'Rh' " +
            "and u.funcionario_estado = 'Ativo' ")
    long countRh();
}