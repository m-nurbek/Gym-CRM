package com.epam.gym.service;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.model.request.TraineeUpdateRequestModel;
import com.epam.gym.dto.model.response.SimpleTrainerResponseModel;
import com.epam.gym.dto.model.response.TraineeResponseModel;
import com.epam.gym.dto.model.response.TraineeUpdateResponseModel;
import com.epam.gym.dto.model.response.TrainingResponseModel;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TraineeService extends CrudService<TraineeDto, BigInteger> {

    Optional<TraineeDto> findByUsername(String username);

    Optional<TraineeResponseModel> findByUsernameToResponse(String username);

    Optional<TraineeUpdateResponseModel> update(String username, TraineeUpdateRequestModel model);

    boolean deleteByUsername(String username);

    Set<TrainerEntity> getTrainers(BigInteger traineeId);

    Set<TrainerEntity> getTrainers(String username);

    Set<TrainerEntity> getUnassignedTrainersByUsername(String username);

    Set<SimpleTrainerResponseModel> getUnassignedTrainersByUsernameToResponse(String username);

    Set<TrainingEntity> getTrainingsByUsername(String username);

    Set<TrainingResponseModel> getTrainingsByUsernameToResponse(String username);

    boolean assignTrainer(BigInteger traineeId, BigInteger trainerId);

    Set<SimpleTrainerResponseModel> updateTrainerListByUsername(String username, List<String> trainerUsernames);

    boolean unassignTrainer(BigInteger traineeId, BigInteger trainerId);
}