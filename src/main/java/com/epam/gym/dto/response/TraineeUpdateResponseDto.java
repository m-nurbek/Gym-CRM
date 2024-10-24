package com.epam.gym.dto.response;

import java.time.LocalDate;
import java.util.List;

public record TraineeUpdateResponseDto(
        String username,
        String firstName,
        String lastName,
        LocalDate dob,
        String address,
        Boolean isActive,
        List<SimpleTrainerResponseDto> trainerList
) {
}