package com.epam.gym.service;

import com.epam.gym.dto.TraineeDto;

import java.util.Optional;

public interface TraineeService {

    Optional<TraineeDto> findByUsername(String username);
}