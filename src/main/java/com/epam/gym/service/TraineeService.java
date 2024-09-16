package com.epam.gym.service;

import com.epam.gym.dto.TraineeDto;

import java.math.BigInteger;
import java.util.Optional;

public interface TraineeService extends CrudService<TraineeDto, BigInteger> {

    Optional<TraineeDto> findByUsername(String username);
}