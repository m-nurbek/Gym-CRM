package com.epam.gym.service;

import com.epam.gym.dto.TrainerDto;

import java.util.Optional;

public interface TrainerService {

    Optional<TrainerDto> findByUsername(String username);
}