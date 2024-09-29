package com.epam.gym.dto.model.request;

public record UserCredentialModel(
        String username,
        String password
) {
}