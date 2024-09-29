package com.epam.gym.dto.model.request;

public record ChangeLoginModel(
        String username,
        String oldPassword,
        String newPassword
) {
}