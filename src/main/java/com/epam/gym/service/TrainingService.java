package com.epam.gym.service;

import com.epam.gym.dto.TrainingDto;
import com.epam.gym.dto.model.request.TrainingAddRequestModel;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;

public interface TrainingService {

    Optional<TrainingDto> findById(BigInteger id);

    boolean save(TrainingAddRequestModel model);

    boolean assignTrainer(String traineeUsername, BigInteger trainerId, String name, String type, LocalDate date, int duration);
}