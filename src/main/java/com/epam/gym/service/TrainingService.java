package com.epam.gym.service;

import com.epam.gym.dto.TrainingDto;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;

public interface TrainingService extends CrudService<TrainingDto, BigInteger> {

    boolean assignTrainer(String traineeUsername, BigInteger trainerId, String name, String type, LocalDate date, int duration);

    boolean unassignTrainer(String traineeUsername, BigInteger trainerId);

    boolean cancelTraining(BigInteger trainingId);
}