package com.epam.gym.service;

import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.model.request.TrainerUpdateRequestModel;
import com.epam.gym.dto.model.response.TrainerResponseModel;
import com.epam.gym.dto.model.response.TrainerUpdateResponseModel;
import com.epam.gym.dto.model.response.TrainingResponseForTrainerModel;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.UserEntity;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface TrainerService extends CrudService<TrainerDto, BigInteger> {

    Optional<TrainerDto> findByUsername(String username);

    Optional<TrainerResponseModel> findByUsernameToResponse(String username);

    Optional<TrainerUpdateResponseModel> update(String username, TrainerUpdateRequestModel model);

    Optional<UserEntity> getUserProfile(BigInteger id);

    Set<TrainingEntity> getTrainingsByUsername(String username);

    Set<TrainingResponseForTrainerModel> getTrainingsByUsernameToResponse(String username, LocalDate periodFrom, LocalDate periodTo, String traineeName);

    boolean assignTrainee(BigInteger trainerId, BigInteger traineeId);

    boolean unassignTrainee(BigInteger trainerId, BigInteger traineeId);
}