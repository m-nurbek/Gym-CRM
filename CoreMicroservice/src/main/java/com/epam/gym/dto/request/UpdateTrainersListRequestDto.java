package com.epam.gym.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author Nurbek on 01.12.2024
 */
public record UpdateTrainersListRequestDto(
        @NotNull(message = "trainerUsernames is required")
        List<String> trainerUsernames
) {
}