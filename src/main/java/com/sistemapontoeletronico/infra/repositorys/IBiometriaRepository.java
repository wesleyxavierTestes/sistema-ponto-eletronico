package com.sistemapontoeletronico.infra.repositorys;

import com.sistemapontoeletronico.domain.entities.biometria.Biometria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBiometriaRepository extends JpaRepository<Biometria, Long>{

}