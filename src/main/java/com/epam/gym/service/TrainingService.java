package com.epam.gym.service;

import com.epam.gym.dto.TrainingDto;
import com.epam.gym.dto.model.request.TrainingAddRequestModel;

import java.math.BigInteger;
import java.time.LocalDate;

public interface TrainingService extends CrudService<TrainingDto, BigInteger> {

    boolean save(TrainingAddRequestModel model);

    boolean assignTrainer(String traineeUsername, BigInteger trainerId, String name, String type, LocalDate date, int duration);

    boolean unassignTrainer(String traineeUsername, BigInteger trainerId);

    boolean cancelTraining(BigInteger trainingId);
}