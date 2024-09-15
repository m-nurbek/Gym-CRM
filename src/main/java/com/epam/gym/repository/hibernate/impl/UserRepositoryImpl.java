package com.epam.gym.repository.hibernate.impl;

import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.hibernate.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends HibernateRepositoryImpl<UserEntity, BigInteger> implements UserRepository {
    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory, SessionFactory sessionFactory1) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory1;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();

        var foundEntity = session.createQuery("from UserEntity where username = :username", UserEntity.class)
                .setParameter("username", username)
                .uniqueResult();

        if (foundEntity == null) {
            return Optional.empty();
        }

        return Optional.of(foundEntity);
    }
}