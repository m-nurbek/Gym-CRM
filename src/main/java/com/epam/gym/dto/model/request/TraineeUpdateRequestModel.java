package com.epam.gym.dto.model.request;

import java.time.LocalDate;

public record TraineeUpdateRequestModel(
        String firstName,
        String lastName,
        LocalDate dob,
        String address,
        Boolean isActive
) {
}