package com.epam.gym.dto.model.response;

import java.time.LocalDate;

public record TrainingResponseForTrainerModel(
        String name,
        LocalDate date,
        String type,
        Integer duration,
        String traineeName
) {
}