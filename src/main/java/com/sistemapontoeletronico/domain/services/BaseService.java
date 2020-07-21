package com.sistemapontoeletronico.domain.services;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import com.sistemapontoeletronico.domain.entities.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<T extends BaseEntity> implements IBaseService<T> {
    private JpaRepository<T, Long> _repository;

    public BaseService(JpaRepository<T, Long> repository) {
        _repository = repository;
    }

    @Override
    public Page<T> findAll(int pagina) {
        Page<T> lista = this._repository.findAll(PageRequest.of((pagina - 1), 10));
        if (!Objects.nonNull(lista)) return null;
        return lista;
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
        try {
            Optional<T> entity = this._repository.findById(id);
            if (!entity.isPresent()) return null;
            return entity.get();
        } catch (Exception e) {
            System.out.println(e);
            return  null;
        }
    }

    @Override
    public T save(T entity) {
        boolean exists = this._repository.existsById(entity.getId());
        if (exists) return null;
        entity.setDataCadastro(LocalDateTime.now());
        this._repository.save(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        T exists = this.findById(entity.getId());
        if (!Objects.nonNull(exists)) return null;
        if (!Objects.nonNull(entity.getDataCadastro()))
            entity.setDataCadastro(exists.getDataCadastro());
        return this._repository.save(entity);
    }

    @Override
    public boolean deleteById(long id) {
        boolean exists = this._repository.existsById(id);
        if (exists) this._repository.deleteById(id);
        return exists;
    }
}