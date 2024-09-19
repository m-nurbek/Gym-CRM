package com.epam.gym.dto;

import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.TrainingTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Set;

@Data
@Builder
@ToString
@AllArgsConstructor
public class TrainingTypeDto implements Dto<BigInteger> {
    private BigInteger id;
    private TrainingTypeEnum name;

    @ToString.Exclude
    private Set<TrainerEntity> trainers;
    @ToString.Exclude
    private Set<TrainingEntity> trainings;
}