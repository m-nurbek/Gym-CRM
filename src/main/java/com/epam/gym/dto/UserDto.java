package com.epam.gym.dto;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Date;

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

    @JsonCreator
    public UserDto(
                   @JsonProperty("firstName") String firstName,
                   @JsonProperty("lastName") String lastName,
                   @JsonProperty("username") String username,
                   @JsonProperty("password") String password,
                   @JsonProperty("isActive") Boolean isActive
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }
}