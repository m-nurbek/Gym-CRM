package com.epam.gym.dto.response;

import java.math.BigInteger;

public record TrainingTypeResponseDto(
        String type,
        BigInteger typeId
) {
}