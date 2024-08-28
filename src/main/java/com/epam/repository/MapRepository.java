package com.epam.repository;

import java.util.Map;
import java.util.Optional;

public interface MapRepository<T, ID> {

    Optional<T> findById(ID id);

    Iterable<T> findAll();

    Iterable<T> findAllById(Iterable<ID> ids);

    boolean existsById(ID id);

    <S extends T> S save(ID id, S entity);

    <S extends T> Iterable<S> saveAll(Map<ID, S> entityMap);

    void update(ID id, T entity);

    void deleteById(ID id);

    void deleteAll();

    long count();

}