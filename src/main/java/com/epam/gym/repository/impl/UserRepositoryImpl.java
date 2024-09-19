package com.epam.gym.repository.impl;

import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.UserRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends HibernateRepositoryImpl<UserEntity, BigInteger> implements UserRepository {
    @PersistenceContext
    private Session session;
    private final TrainerRepository trainerRepository;

    public UserRepositoryImpl(TrainerRepository trainerRepository) {
        super();
        this.trainerRepository = trainerRepository;
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

    @Override
    public boolean isUsernameAndPasswordMatch(String username, String password) {
        var foundEntity = session.createQuery("from UserEntity where username = :username and password = :password", UserEntity.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();

        return foundEntity != null;
    }

    @Override
    public boolean deleteById(BigInteger id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        var entity = session.get(UserEntity.class, id);

        if (entity != null) {
            if (entity.getTrainer() != null) {
                trainerRepository.deleteById(entity.getTrainer().getId());
            }

            session.remove(entity);
            return true;
        }

        return false;
    }
}