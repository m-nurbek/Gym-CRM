package com.epam.gym.dto;

import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDto implements Dto<BigInteger> {
    private BigInteger id;
    private TrainingTypeEntity specialization;
    private UserEntity user;
    private List<TrainingEntity> trainings;
}