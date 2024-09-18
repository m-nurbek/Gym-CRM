package com.epam.gym.repository.impl;

import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.repository.TrainerRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.math.BigInteger;

@Repository
public class TrainerRepositoryImpl extends HibernateRepositoryImpl<TrainerEntity, BigInteger> implements TrainerRepository {
    @PersistenceContext
    private Session session;

    public TrainerRepositoryImpl() {
        super();
    }

    @Override
    public boolean deleteById(BigInteger id) {
        Assert.notNull(id, HibernateRepositoryImpl.ID_MUST_NOT_BE_NULL);

        var entity = session.get(TrainerEntity.class, id);

        if (entity != null) {
            session.createNativeQuery("""
                DELETE FROM trainee_trainer as t1
                WHERE t1.trainer_id = :trainerIdToDelete AND NOT EXISTS (
                    SELECT t2.trainer_id FROM trainee_trainer as t2
                    WHERE t2.trainer_id != :trainerIdToDelete AND t2.trainee_id = t1.trainee_id
                );
            """, TrainerEntity.class).setParameter("trainerIdToDelete", id).executeUpdate();
            return true;
        }

        return false;
    }
}