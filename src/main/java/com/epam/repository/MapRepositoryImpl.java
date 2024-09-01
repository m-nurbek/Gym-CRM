package com.epam.repository;

import com.epam.entity.Entity;
import com.epam.util.AtomicBigInteger;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapRepositoryImpl<T extends Entity<ID>, ID extends BigInteger> implements MapRepository<T, ID> {
    private static final String ID_MUST_NOT_BE_NULL = "ID must not be null";
    private static final String IDS_MUST_NOT_BE_NULL = "IDs must not be null";
    private static final String ENTITY_MUST_NOT_BE_NULL = "Entity must not be null";
    private static final String ENTITIES_MUST_NOT_BE_NULL = "Entities must not be null";

    private final Map<ID, T> map;

    private final Storage<T, ID> storage;
    private final AtomicBigInteger idGenerator = new AtomicBigInteger(BigInteger.ZERO);

    public MapRepositoryImpl(Storage<T, ID> storage) {
        this.storage = storage;
        this.map = storage.getMap();
    }

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
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

        if (entity.getId() != null && map.containsKey(entity.getId())) {
            return (S) map.get(entity.getId());
        }

        if (entity.getId() == null) {
            ID id = generateId();

            while (map.containsKey(id)) {
                id = generateId();
            }

            entity.setId(id);
        }

        map.putIfAbsent(entity.getId(), entity);

        return entity;
    }

    private ID generateId() {
        try {
            return (ID) idGenerator.incrementAndGet();
        } catch (Exception e) {
            throw new IllegalStateException("ID type is not supported for generation", e);
        }
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, ENTITIES_MUST_NOT_BE_NULL);

        var entitiesIterator = entities.iterator();

        while (entitiesIterator.hasNext()) {
            save(entitiesIterator.next());
        }

        return entities;
    }

    @Override
    public void update(ID id, T entity) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

        if (map.containsKey(id)) {
            map.put(id, entity);
        }
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