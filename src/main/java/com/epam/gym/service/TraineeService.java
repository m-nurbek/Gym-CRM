package com.epam.gym.service;

import com.epam.gym.dto.request.TraineeUpdateRequestDto;
import com.epam.gym.dto.response.SimpleTrainerResponseDto;
import com.epam.gym.dto.response.TraineeResponseDto;
import com.epam.gym.dto.response.TraineeUpdateResponseDto;
import com.epam.gym.dto.response.TrainingResponseForTraineeDto;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TraineeService {

    TraineeResponseDto save(LocalDate dob, String address, BigInteger userId);

    Optional<TraineeResponseDto> findByUsername(String username);

    Optional<TraineeUpdateResponseDto> update(String username, TraineeUpdateRequestDto model);

    boolean deleteByUsername(String username);

    Set<SimpleTrainerResponseDto> getUnassignedTrainersByUsernameToResponse(String username);

    Set<TrainingResponseForTraineeDto> getTrainingsByUsernameToResponse(String username, LocalDate periodFrom, LocalDate periodTo, String trainerName, String trainingType);

    Set<SimpleTrainerResponseDto> updateTrainerListByUsername(String username, List<String> trainerUsernames);
}