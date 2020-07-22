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

        /**
         * Verifica se existe um específico funcionário que esteja ativo 
         * e seja do Setor Rh
         * e que o acesso seja o digitado
         * @return Quantidade, Se for Maior que 0 então existe
         */
        @Query(nativeQuery = true, value = "select count(*) from public.funcionario as u "
                        + "where u.funcionario_setor = 'Rh' " + "and u.funcionario_estado = 'Ativo' "
                        + "and u.id = :id " + "and u.acesso = :acesso " + " LIMIT 1")
        int existsByIdRh(@Param("id") long id, @Param("acesso") String acesso);

        /**
         * Faz contagem de funcionários que esteja ativo e sejam do Setor Rh
         * @return Contagem
         */
        @Query(nativeQuery = true, value = "select count(*) from public.funcionario as u "
                        + "where u.funcionario_setor = 'Rh' " + "and u.funcionario_estado = 'Ativo' ")
        long countRh();

        /**
         * 
         * @param estado
         * @param pageable
         * @return 
         */
        @Query(nativeQuery = true, value = "SELECT * FROM public.funcionario as f "
                        + "where f.funcionario_estado like :estado "
                        + "ORDER BY id",
                        countQuery = "SELECT count(f.*) FROM public.funcionario as f "
                                        + "where f.funcionario_estado like :estado "
                                        + "ORDER BY id")
        Page<Funcionario> findAllFuncionarioEstado(@Param("estado") String estado, Pageable pageable);

        /**
         * Busca um Funcionário que esteja Ativo
         * e tenha biometria cadastrada 
         * com base no código da biometria
         * @return funcionário
         */
        @Query(nativeQuery = true, value = "SELECT * FROM public.funcionario as f "
                                        + "join public.biometria b "
                                        + "on b.funcionario_id = f.id "
                                        + "and f.funcionario_estado = 'Ativo' "
                                        + "where b.codigo like :acesso limit 1")
        Funcionario findByCodigoAcessoBiometria(@Param("acesso") String codigoAcesso);

        /**
         * Busca um Funcionário que esteja Ativo e com acesso via código manual esteja habilitado
         * com base no código de acesso digitado pelo funcionário
         * Usado limite de 1 para evitar possíveis duplicações no Sistema.
         * @return Funcionário
         */
        @Query(nativeQuery = true, value = "SELECT * FROM public.funcionario as f "
                        + "where f.acesso like :acesso "
                        + "and f.funcionario_estado = 'Ativo' "
                        + "and f.acesso_bloqueado = false "
                        +" limit 1")
        Funcionario findByCodigoAcessoManual(@Param("acesso") String codigoAcesso);

        /**
         * Busca uma Paginação de Funcionários qual não tenha biometria cadastrada
         * com base na página enviado
         * @return Pagina de funcionário
         */
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