package com.epam.gym.repository;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, BigInteger> {

    Optional<TrainingEntity> findByTraineeAndTrainer(@NonNull TraineeEntity traineeEntity, @NonNull TrainerEntity trainerEntity);
}