package com.epam.gym.repository.impl;

import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.UserRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends HibernateRepositoryImpl<UserEntity, BigInteger> implements UserRepository {
    @PersistenceContext
    private Session session;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {

        var foundEntity = session.createQuery("from UserEntity where username = :username", UserEntity.class)
                .setParameter("username", username)
                .uniqueResult();

        if (foundEntity == null) {
            return Optional.empty();
        }

        return Optional.of(foundEntity);
    }

    @Override
    public boolean updateActiveState(BigInteger id, boolean isActive) {
        var user = session.get(UserEntity.class, id);

        if (user == null) {
            return false;
        }

        user.setIsActive(isActive);
        session.merge(user);

        return true;
    }

    @Override
    public boolean updatePassword(BigInteger id, String newPassword) {
        var user = session.get(UserEntity.class, id);

        if (user == null) {
            return false;
        }

        user.setPassword(newPassword);
        session.merge(user);

        return true;
    }
}