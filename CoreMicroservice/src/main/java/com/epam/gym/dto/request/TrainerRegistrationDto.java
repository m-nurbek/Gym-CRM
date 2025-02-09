package com.epam.gym.dto.request;

import com.epam.gym.entity.TrainingTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TrainerRegistrationDto(
        @NotBlank(message = "First name is required")
        @Size(min = 2, message = "First name must have at least 2 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, message = "Last name must have at least 2 characters")
        String lastName,

        @NotNull(message = "Specialization is required")
        TrainingTypeEnum specialization,

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}",
                message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
        String password
) {
}