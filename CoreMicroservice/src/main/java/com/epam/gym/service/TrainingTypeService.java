package com.epam.gym.service;

import com.epam.gym.dto.response.TrainingTypeResponseDto;

import java.util.Set;

public interface TrainingTypeService {

    Set<TrainingTypeResponseDto> getAllToResponse();
}