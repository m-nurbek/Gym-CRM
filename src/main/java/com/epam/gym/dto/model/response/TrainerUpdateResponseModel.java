package com.epam.gym.dto.model.response;

import java.util.List;

public record TrainerUpdateResponseModel(
        String username,
        String firstName,
        String lastName,
        String specialization,
        Boolean isActive,
        List<SimpleTraineeResponseModel> traineeList
) {
}