package com.epam.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TraineeRegistrationDto(
        @NotBlank(message = "First name is required")
        @Size(min = 2, message = "First name must have at least 2 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, message = "Last name must have at least 2 characters")
        String lastName,

        @Past(message = "Date of birth must be in the past")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate dob,

        String address,

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}",
                message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
        String password
) {
}