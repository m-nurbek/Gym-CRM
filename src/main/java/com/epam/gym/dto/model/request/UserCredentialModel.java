package com.epam.gym.dto.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCredentialModel(
        @NotBlank(message = "Username is mandatory")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {
}