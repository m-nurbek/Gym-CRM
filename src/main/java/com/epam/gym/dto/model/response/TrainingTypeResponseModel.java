package com.epam.gym.dto.model.response;

import java.math.BigInteger;

public record TrainingTypeResponseModel(
        String type,
        BigInteger typeId
) {
}