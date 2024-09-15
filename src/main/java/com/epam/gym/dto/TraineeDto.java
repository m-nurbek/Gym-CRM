package com.epam.gym.dto;

import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@ToString
@AllArgsConstructor
public class TraineeDto implements Dto<BigInteger> {
    private BigInteger id;
    private LocalDate dob;
    private String address;
    private UserEntity user;
    private List<TrainingEntity> trainings;
}