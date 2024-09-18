package com.epam.gym.repository;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface TrainingRepository extends HibernateRepository<TrainingEntity, BigInteger> {

    Optional<TrainingEntity> findByTraineeAndTrainer(TraineeEntity traineeEntity, TrainerEntity trainerEntity);
}