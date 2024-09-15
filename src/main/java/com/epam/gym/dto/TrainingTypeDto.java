package com.epam.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@ToString
@AllArgsConstructor
public class TrainingTypeDto implements Dto<BigInteger> {
    private BigInteger id;
    private String name;
    private List<TrainerDto> trainers;
    private List<TrainingDto> trainings;

    @Deprecated(since = "2024-09-12", forRemoval = false)
    public TrainingTypeDto(BigInteger id, String name) {
        this.id = id;
        this.name = name;
    }
}