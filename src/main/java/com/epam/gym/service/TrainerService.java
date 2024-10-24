package com.epam.gym.service;

import com.epam.gym.dto.request.TrainerUpdateRequestDto;
import com.epam.gym.dto.response.TrainerResponseDto;
import com.epam.gym.dto.response.TrainerUpdateResponseDto;
import com.epam.gym.dto.response.TrainingResponseForTrainerDto;
import com.epam.gym.entity.TrainingTypeEnum;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;

public interface TrainerService {

    TrainerResponseDto save(TrainingTypeEnum specialization, BigInteger userId);

    TrainerResponseDto findByUsername(String username);

    TrainerUpdateResponseDto update(String username, TrainerUpdateRequestDto model);

    Set<TrainingResponseForTrainerDto> getTrainingsByUsernameToResponse(String username, LocalDate periodFrom, LocalDate periodTo, String traineeName);
}