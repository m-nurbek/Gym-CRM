package com.epam.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@Builder
@ToString
@AllArgsConstructor
public class TrainingDto implements Dto<BigInteger> {
    private BigInteger id;
    private TraineeDto trainee;
    private TrainerDto trainer;
    private String name;
    private TrainingTypeDto type;
    private LocalDate date;
    private int duration;
}