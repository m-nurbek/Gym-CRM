package com.epam.repository;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MapRepositoryImpl <T, ID> implements MapRepository<T, ID> {

    private static final String ID_MUST_NOT_BE_NULL = "ID must not be null";
    private static final String IDS_MUST_NOT_BE_NULL = "IDs must not be null";
    private static final String ENTITY_MUST_NOT_BE_NULL = "Entity must not be null";
    private static final String ENTITIES_MUST_NOT_BE_NULL = "Entities must not be null";

    private final Map<ID, T> map = new ConcurrentHashMap<>();

    @Override
    public Optional<T> findById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!map.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(map.get(id));
    }

    @Override
    public Iterable<T> findAll() {
        return map.values();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        Assert.notNull(ids, IDS_MUST_NOT_BE_NULL);

        List<T> result = new ArrayList<>();

        for (ID id : ids) {
            if (map.containsKey(id)) {
                result.add(map.get(id));
            }
        }

        return result;
    }

    @Override
    public boolean existsById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return map.containsKey(id);
    }


    @Override
    public <S extends T> S save(ID id, S entity) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

        map.put(id, entity);

        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Map<ID, S> entityMap) {
        Assert.notNull(entityMap, ENTITIES_MUST_NOT_BE_NULL);

        map.putAll(entityMap);

        List<S> result = new ArrayList<>();
        for (ID id : entityMap.keySet()) {
            result.add(entityMap.get(id));
        }

        return result;
    }

    @Override
    public void update(ID id, T entity) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

        map.put(id, entity);
    }

    @Override
    public void deleteById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        map.remove(id);
    }

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public long count() {
        return map.size();
    }
}