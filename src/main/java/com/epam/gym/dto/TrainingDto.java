package com.epam.gym.dto;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingTypeEntity;
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
    @ToString.Exclude
    private TraineeEntity trainee;
    @ToString.Exclude
    private TrainerEntity trainer;
    private String name;
    @ToString.Exclude
    private TrainingTypeEntity type;
    private LocalDate date;
    private int duration;
}