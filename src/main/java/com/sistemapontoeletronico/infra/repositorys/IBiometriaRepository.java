package com.sistemapontoeletronico.infra.repositorys;

import com.sistemapontoeletronico.domain.entities.biometria.Biometria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBiometriaRepository extends JpaRepository<Biometria, Long>{

    @Query(
            nativeQuery = true,
            value = "select u.funcionario_id from public.biometria as u " +
                    "where u.codigo = ?1 " +
                    "LIMIT 1")
    public Long findFuncionarioIdByBiometriaCodigo(String codigo);
}