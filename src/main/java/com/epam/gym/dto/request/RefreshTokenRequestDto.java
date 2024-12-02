package com.epam.gym.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(
        @NotBlank(message = "refreshToken is required")
        String refreshToken
) {
}