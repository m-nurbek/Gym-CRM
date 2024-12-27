package com.epam.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author Nurbek on 03.12.2024
 */
public record TrainerWorkloadRequest(
        @NotBlank(message = "username is required")
        String username,
        @NotBlank(message = "firstName is required")
        String firstName,
        @NotBlank(message = "lastName is required")
        String lastName,
        boolean isActive,
        @NotNull(message = "trainingDate is required")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate trainingDate,
        int duration,
        @NotNull(message = "actionType is required")
        ActionType actionType
) {
}