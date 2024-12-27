package com.epam.gym.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TrainingAddRequestDto(
        @NotBlank(message = "Trainee username is required")
        @Size(min = 3, max = 20, message = "Trainee username must be between 3 and 20 characters")
        String traineeUsername,

        @NotBlank(message = "Trainer username is required")
        @Size(min = 3, max = 20, message = "Trainer username must be between 3 and 20 characters")
        String trainerUsername,

        @NotBlank(message = "Training name is required")
        @Size(min = 1, max = 30, message = "Training name must be between 1 and 30 characters")
        String trainingName,

        @FutureOrPresent(message = "Date of birth must be in the future or present")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "Training duration is required")
        Integer duration
) {
}