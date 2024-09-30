package com.epam.gym.dto.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangeLoginModel(
        @NotBlank(message = "Username is mandatory")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,

        @NotBlank(message = "Password is required")
        String oldPassword,

        @NotBlank(message = "Password is required")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}",
                message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
        String newPassword
) {
}