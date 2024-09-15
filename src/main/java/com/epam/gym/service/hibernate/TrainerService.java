package com.epam.gym.service.hibernate;

import com.epam.gym.dto.TrainerDto;

import java.util.Optional;

public interface TrainerService {

    Optional<TrainerDto> findByUsername(String username);
}