package com.sistemapontoeletronico.domain.services;

import java.util.List;

import com.sistemapontoeletronico.domain.entities.BaseEntity;

public interface IBaseService<T extends BaseEntity>  {

	List<T> findAll(int pagina);
    long count();
    boolean existsById(long id);
    T findById(long id);
    T save(T entity);
    T update(T entity);
    boolean deleteById(long id);
}