package com.epam.gym.dto.response;

public record JwtTokenResponseDto(
        String accessToken,
        String refreshToken
) {
}