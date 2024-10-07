package com.epam.gym.dto;

import java.math.BigInteger;

/**
 * DTO for {@link com.epam.gym.entity.UserEntity}
 */
public record UserDto(
        BigInteger id,
        String firstName,
        String lastName,
        String username,
        String password,
        Boolean isActive
) {
}