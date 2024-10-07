package com.epam.gym.dto.response;

import java.util.List;

public record TrainerResponseDto(
        String firstName,
        String lastName,
        String specialization,
        Boolean isActive,
        List<SimpleTraineeResponseDto> traineeList
) {
}