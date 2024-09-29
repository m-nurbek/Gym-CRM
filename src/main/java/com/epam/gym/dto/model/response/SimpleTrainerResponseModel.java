package com.epam.gym.dto.model.response;

public record SimpleTrainerResponseModel(
        String username,
        String firstName,
        String lastName,
        String specialization
) {
}