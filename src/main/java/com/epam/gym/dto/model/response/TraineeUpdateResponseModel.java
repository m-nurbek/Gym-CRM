package com.epam.gym.dto.model.response;

import java.time.LocalDate;
import java.util.List;

public record TraineeUpdateResponseModel(
        String username,
        String firstName,
        String lastName,
        LocalDate dob,
        String address,
        Boolean isActive,
        List<SimpleTrainerResponseModel> trainerList
) {
}