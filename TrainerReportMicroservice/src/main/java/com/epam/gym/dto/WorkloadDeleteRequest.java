package com.epam.gym.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author Nurbek on 14.12.2024
 */
public record WorkloadDeleteRequest(
        @NotNull(message = "trainerUsernames is required")
        List<String> trainerUsernames
) {
}