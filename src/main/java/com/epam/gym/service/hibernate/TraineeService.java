package com.epam.gym.service.hibernate;

import com.epam.gym.dto.TraineeDto;

import java.util.Optional;

public interface TraineeService {

    Optional<TraineeDto> findByUsername(String username);
}