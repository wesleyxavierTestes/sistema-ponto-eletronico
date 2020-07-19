package com.sistemapontoeletronico.infra.repositorys;

import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRelogioPontoRepository extends JpaRepository<RelogioPonto, Long>{

}