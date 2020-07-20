package com.sistemapontoeletronico.infra.repositorys;

import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPreDefinicaoPontoRepository extends JpaRepository<PreDefinicaoPonto, Long>{
    
}