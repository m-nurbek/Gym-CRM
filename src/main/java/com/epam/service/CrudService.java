package com.epam.service;

import java.util.List;

public interface CrudService<T, ID> {
    void add(T obj);

    void update(T obj);

    void delete(ID id);

    T get(ID id);

    List<T> getAll();
}