package com.epam.gym.dto.model.response;

import java.time.LocalDate;

public record TrainingResponseForTraineeModel(
        String name,
        LocalDate date,
        String type,
        Integer duration,
        String trainerName
) {
}