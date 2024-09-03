package com.epam.gym.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    T add(T obj);

    T update(T obj);

    void delete(ID id);

    Optional<T> get(ID id);

    List<T> getAll();
}