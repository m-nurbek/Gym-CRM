package com.epam.gym.repository.hibernate;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HibernateRepository<T, ID> {
    Optional<T> findById(ID id);

    Iterable<T> findAll();

    boolean existsById(ID id);

    T save(T entity);

    Iterable<T> saveAll(Iterable<T> entities);

    boolean update(T entity);

    boolean deleteById(ID id);

    void deleteAll();

    long count();
}