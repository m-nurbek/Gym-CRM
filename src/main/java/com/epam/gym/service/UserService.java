package com.epam.gym.service;

import com.epam.gym.dto.UserDto;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> findByUsername(String username);

    boolean changePassword(String oldPassword, String newPassword);

    UserDto updateProfile(UserDto updatedUser);

    void updateActiveState(boolean isActive);
}