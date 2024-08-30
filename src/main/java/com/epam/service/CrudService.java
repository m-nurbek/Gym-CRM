package com.epam.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    void add(T obj);

    void update(T obj);

    void delete(ID id);

    Optional<T> get(ID id);

    List<T> getAll();
}