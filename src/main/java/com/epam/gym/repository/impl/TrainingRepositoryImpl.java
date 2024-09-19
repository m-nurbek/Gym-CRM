package com.epam.gym.repository.impl;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.repository.TrainingRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public class TrainingRepositoryImpl extends HibernateRepositoryImpl<TrainingEntity, BigInteger> implements TrainingRepository {
    @PersistenceContext
    private Session session;

    public TrainingRepositoryImpl() {
        super();
    }

    @Override
    public Optional<TrainingEntity> findByTraineeAndTrainer(TraineeEntity traineeEntity, TrainerEntity trainerEntity) {
        var query = session.createQuery("from TrainingEntity where trainee = :trainee and trainer = :trainer", TrainingEntity.class);
        query.setParameter("trainee", traineeEntity);
        query.setParameter("trainer", trainerEntity);

        return Optional.ofNullable(query.uniqueResult());
    }
}