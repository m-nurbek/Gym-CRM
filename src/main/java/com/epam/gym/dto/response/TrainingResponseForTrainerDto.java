package com.epam.gym.dto.response;

import java.time.LocalDate;

public record TrainingResponseForTrainerDto(
        String name,
        LocalDate date,
        String type,
        Integer duration,
        String traineeName
) {
}