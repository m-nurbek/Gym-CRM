package com.epam.gym.dto;

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
    private UserDto user;
    private List<TrainingDto> trainings;
    private List<TrainerDto> trainers;

    @Deprecated(since = "2024-09-12", forRemoval = false)
    public TraineeDto(BigInteger id, LocalDate dob, String address, UserDto user) {
        this.id = id;
        this.dob = dob;
        this.address = address;
        this.user = user;
    }
}