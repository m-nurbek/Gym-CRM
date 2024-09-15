package com.epam.gym.repository.hibernate.impl;

import com.epam.gym.entity.EntityInterface;
import com.epam.gym.repository.hibernate.HibernateRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

@Transactional(isolation = Isolation.READ_COMMITTED)
public class HibernateRepositoryImpl<T extends EntityInterface<ID>, ID> implements HibernateRepository<T, ID> {
    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;

    private static final String ID_MUST_NOT_BE_NULL = "ID must not be null";
    private static final String ENTITY_MUST_NOT_BE_NULL = "Entity must not be null";
    private static final String ENTITIES_MUST_NOT_BE_NULL = "Entities must not be null";

    public HibernateRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        entityClass = getDomainClass();
    }

    /**
     * Method to get the class of the entity
     *
     * @return
     */
    private Class<T> getDomainClass() {
        Class<T> entityClass = null;

        Type genericSuperclass = getClass().getGenericSuperclass();

        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            entityClass = (Class<T>) actualTypeArguments[0];
        }

        return entityClass;
    }

    @Override
    public Optional<T> findById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Session session = sessionFactory.getCurrentSession();
        var foundEntity = session.get(entityClass, id);

        if (foundEntity == null) {
            return Optional.empty();
        }

        return Optional.of(foundEntity);
    }

    @Override
    public Iterable<T> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from %s".formatted(entityClass.getSimpleName()), entityClass).list();
    }

    @Override
    public boolean existsById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery("SELECT COUNT(e) FROM %s e WHERE e.id = :id".formatted(entityClass.getSimpleName()), Long.class)
                .setParameter("id", id)
                .uniqueResult();

        return count != null && count > 0;
    }

    @Override
    public T save(T entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

        if (entity.getId() != null) {
            var e = findById(entity.getId());

            if (e.isPresent()) {
                return e.get();
            }
        }

        Session session = sessionFactory.getCurrentSession();
        session.save(entity);
        session.flush();
        session.refresh(entity);
        return entity;
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> entities) {
        Assert.notNull(entities, ENTITIES_MUST_NOT_BE_NULL);

        for (T entity : entities) {
            save(entity);
        }

        return entities;
    }

    @Override
    public boolean update(T entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);
        Assert.notNull(entity.getId(), ID_MUST_NOT_BE_NULL);

        if (!existsById(entity.getId())) {
            return false;
        }

        Session session = sessionFactory.getCurrentSession();
        session.merge(entity);
        session.flush();
        session.refresh(entity);

        return true;
    }

    @Override
    public boolean deleteById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Session session = sessionFactory.getCurrentSession();
        var entity = session.get(entityClass, id);

        if (entity != null) {
            session.remove(entity);
            return true;
        }

        return false;
    }

    @Override
    public void deleteAll() {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("DELETE FROM %s".formatted(entityClass.getSimpleName())).executeUpdate();
        session.flush();
    }

    @Override
    public long count() {
        Session session = sessionFactory.getCurrentSession();

        Long count = session.createQuery("SELECT COUNT(e) FROM %s e".formatted(entityClass.getSimpleName()), Long.class).uniqueResult();

        return count != null ? count : 0;
    }
}