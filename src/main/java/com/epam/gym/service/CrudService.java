package com.epam.gym.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    T save(T obj);

    boolean update(T obj);

    boolean delete(ID id);

    Optional<T> get(ID id);

    List<T> getAll();

    long count();
}