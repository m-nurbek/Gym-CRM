package com.epam.gym.service;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;

public interface TraineeService extends CrudService<TraineeDto, BigInteger> {

    Optional<TraineeDto> findByUsername(String username);

    Set<TrainerEntity> getTrainers(BigInteger traineeId);

    Set<TrainerEntity> getTrainers(String username);

    Set<TrainerEntity> getUnassignedTrainersByUsername(String username);

    Set<TrainingEntity> getTrainingsByUsername(String username);

    boolean assignTrainer(BigInteger traineeId, BigInteger trainerId);

    boolean unassignTrainer(BigInteger traineeId, BigInteger trainerId);
}