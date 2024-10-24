package com.epam.gym.service;

import com.epam.gym.dto.request.TrainingAddRequestDto;

public interface TrainingService {

    void save(TrainingAddRequestDto model);
}