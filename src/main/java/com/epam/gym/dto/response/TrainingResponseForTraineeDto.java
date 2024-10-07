package com.epam.gym.dto.response;

import java.time.LocalDate;

public record TrainingResponseForTraineeDto(
        String name,
        LocalDate date,
        String type,
        Integer duration,
        String trainerName
) {
}