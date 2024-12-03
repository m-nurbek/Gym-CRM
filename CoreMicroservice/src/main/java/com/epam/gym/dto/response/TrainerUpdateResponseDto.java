package com.epam.gym.dto.response;

import java.util.List;

public record TrainerUpdateResponseDto(
        String username,
        String firstName,
        String lastName,
        String specialization,
        Boolean isActive,
        List<SimpleTraineeResponseDto> traineeList
) {
}