package com.epam.gym.dto;

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
    private TrainingTypeDto specialization;
    private UserDto user;
    private List<TrainingDto> trainings;
    private List<TraineeDto> trainees;

    @Deprecated(since = "2024-09-12", forRemoval = false)
    public TrainerDto(BigInteger id, TrainingTypeDto specialization, UserDto user) {
        this.id = id;
        this.specialization = specialization;
        this.user = user;
    }
}