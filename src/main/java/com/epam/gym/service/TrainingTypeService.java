package com.epam.gym.service;

import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.dto.model.response.TrainingTypeResponseModel;
import com.epam.gym.entity.TrainingTypeEnum;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainingTypeService {
    List<TrainingTypeDto> getAll();

    Set<TrainingTypeResponseModel> getAllToResponse();

    Optional<TrainingTypeDto> getTrainingTypeName(String name);

    Optional<TrainingTypeDto> getTrainingTypeName(TrainingTypeEnum name);
}