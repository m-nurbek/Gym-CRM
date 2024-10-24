package com.epam.gym.dto.response;

public record SimpleTrainerResponseDto(
        String username,
        String firstName,
        String lastName,
        String specialization
) {
}