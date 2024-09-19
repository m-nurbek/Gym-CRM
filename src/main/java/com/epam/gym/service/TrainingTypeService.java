package com.epam.gym.service;

import com.epam.gym.dto.TrainingTypeDto;

import java.math.BigInteger;
import java.util.Optional;

public interface TrainingTypeService extends CrudService<TrainingTypeDto, BigInteger> {

    Optional<TrainingTypeDto> getTrainingTypeName(String name);
}