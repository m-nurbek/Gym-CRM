package com.epam.gym.dto.model.request;

import com.epam.gym.entity.TrainingTypeEnum;

public record TrainerRegistrationModel(
        String firstName,
        String lastName,
        TrainingTypeEnum specialization
) {
}