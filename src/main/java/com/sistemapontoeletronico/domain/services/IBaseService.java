package com.sistemapontoeletronico.domain.services;

import java.util.List;

import com.sistemapontoeletronico.domain.entities.BaseEntity;

import org.springframework.data.domain.Page;

public interface IBaseService<T extends BaseEntity>  {

	Page<T> findAll(int pagina);
    long count();
    boolean existsById(long id);
    T findById(long id);
    T save(T entity);
    T update(T entity);
    boolean deleteById(long id);
}