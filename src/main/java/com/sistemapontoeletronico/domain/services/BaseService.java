package com.sistemapontoeletronico.domain.services;

import java.util.List;
import java.util.Optional;
import com.sistemapontoeletronico.domain.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<T extends BaseEntity> implements IBaseService<T> {
    protected  JpaRepository<T, Long> _repository;

    public BaseService(JpaRepository<T, Long> repository) {
        _repository = repository;
    }

    @Override
    public List<T> findAll() {
        List<T> listaEntity = this._repository.findAll();
        return listaEntity;
    }

    @Override
    public long count() {
        long count = this._repository.count();
        return count;
    }

    @Override
    public boolean existsById(long id) {
        boolean entityExists = this._repository.existsById(id);
        return entityExists;
    }

    @Override
    public T findById(long id) {
        Optional<T> entity = this._repository.findById(id);
        if (!entity.isPresent()) return null;
        return entity.get();
    }

    @Override
    public T save(T entity) {
        boolean exists = this._repository.existsById(entity.getId());
        if (exists) return null;
        this._repository.save(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        boolean exists = this._repository.existsById(entity.getId());
        if (!exists) return null;
        return this._repository.save(entity);
    }

    @Override
    public boolean deleteById(long id) {
        boolean exists = this._repository.existsById(id);
        if (exists) this._repository.deleteById(id);
        return exists;
    }
}