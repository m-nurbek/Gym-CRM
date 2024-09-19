package com.epam.gym.dto;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;

@Data
@Builder
@ToString
@AllArgsConstructor
public class UserDto implements Dto<BigInteger> {
    private BigInteger id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;
    @ToString.Exclude
    private TraineeEntity trainee;
    @ToString.Exclude
    private TrainerEntity trainer;
}