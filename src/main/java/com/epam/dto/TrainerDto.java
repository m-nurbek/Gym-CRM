package com.epam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class TrainerDto {
    private BigInteger id;
    private TrainingTypeDto specialization;
    private UserDto user;
}