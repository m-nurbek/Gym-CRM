package com.epam.repository;

import java.util.Optional;

public interface MapRepository<T, ID> {

    Optional<T> findById(ID id);

    Iterable<T> findAll();

    Iterable<T> findAllById(Iterable<ID> ids);

    boolean existsById(ID id);

    <S extends T> S save(S entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    void update(ID id, T entity);

    void deleteById(ID id);

    void deleteAll();

    long count();

}