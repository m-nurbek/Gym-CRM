package com.epam.gym.service;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.entity.TrainerEntity;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;

public interface TraineeService extends CrudService<TraineeDto, BigInteger> {

    Optional<TraineeDto> findByUsername(String username);

    Set<TrainerEntity> getTrainers(BigInteger traineeId);

    Set<TrainerEntity> getUnassignedTrainersByUsername(String username);
}