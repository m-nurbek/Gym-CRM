package com.epam.gym.dto.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationModel(
        @NotBlank(message = "First name is required")
        @Size(min = 2, message = "First name must have at least 2 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, message = "Last name must have at least 2 characters")
        String lastName
) {
}