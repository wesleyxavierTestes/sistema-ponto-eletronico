package com.sistemapontoeletronico.infra.repositorys;

import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
                        + "where f.funcionario_estado like :estado "
                        + "ORDER BY id",
                        countQuery = "SELECT count(f.*) FROM public.funcionario as f "
                                        + "where f.funcionario_estado like :estado "
                                        + "ORDER BY id")
        Page<Funcionario> findAllFuncionarioEstado(@Param("estado") String estado, Pageable pageable);

        @Query(nativeQuery = true, value = "SELECT * FROM public.funcionario as f "
                                        + "join public.biometria b "
                                        + "on b.funcionario_id = f.id "
                                        + "and f.funcionario_estado = 'Ativo' "
                                        + "and f.acesso_bloqueado = false "
                                        + "where b.codigo like :acesso limit 1")
        Funcionario findByCodigoAcessoBiometria(@Param("acesso") String codigoAcesso);

        @Query(nativeQuery = true, value = "SELECT * FROM public.funcionario as f "
                        + "where f.acesso like :acesso "
                        + "and f.funcionario_estado = 'Ativo' "
                        + "and f.acesso_bloqueado = false "
                        +" limit 1")
        Funcionario findByCodigoAcessoManual(@Param("acesso") String codigoAcesso);


        @Query(nativeQuery = true, 
        value = "select * from public.funcionario fu "
                        + "where fu.id NOT IN (SELECT f.id "
                                                + "FROM public.funcionario as f "
                                                  +"join public.biometria b "
                                                  + "on b.funcionario_id = f.id) "
                                                  + "ORDER BY id",
        countQuery = "select * from public.funcionario fu "
                        + "where fu.id NOT IN (SELECT f.id "
                                + "FROM public.funcionario as f "
                                  +"join public.biometria b "
                                  + "on b.funcionario_id = f.id) "
                                  + "ORDER BY id")
	Page<Funcionario> findAllFuncionarioBiometriaFaltante(PageRequest of);
}