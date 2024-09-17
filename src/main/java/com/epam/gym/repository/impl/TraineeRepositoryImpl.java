package com.epam.gym.repository.impl;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.repository.TraineeRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Repository
public class TraineeRepositoryImpl extends HibernateRepositoryImpl<TraineeEntity, BigInteger> implements TraineeRepository {
    @PersistenceContext
    private Session session;

    public TraineeRepositoryImpl() {
        super();
    }

    @Override
    public Set<TrainerEntity> getUnassignedTrainersByUsername(String username) {
        var query = session.createQuery("""
                        from TrainerEntity as trainer
                        where trainer not in
                            (select trainee.trainers from TraineeEntity trainee, UserEntity user
                             where user.username = :username AND user = trainee.user)
                        """, TrainerEntity.class)
                .setParameter("username", username).list();

        return new HashSet<>(query);
    }
}