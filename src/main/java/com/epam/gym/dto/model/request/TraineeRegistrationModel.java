package com.epam.gym.dto.model.request;

import java.time.LocalDate;

public record TraineeRegistrationModel(
        String firstName,
        String lastName,
        LocalDate dob,
        String address
) {
}