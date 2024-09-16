package com.epam.gym.service;

import com.epam.gym.dto.TrainerDto;

import java.math.BigInteger;
import java.util.Optional;

public interface TrainerService extends CrudService<TrainerDto, BigInteger> {

    Optional<TrainerDto> findByUsername(String username);
}