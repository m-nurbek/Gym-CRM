package com.epam.gym.dto.model.request;

import java.time.LocalDate;

public record TrainingAddRequestModel(
        String traineeUsername,
        String trainerUsername,
        String trainingName,
        LocalDate date,
        Integer duration
) {
}