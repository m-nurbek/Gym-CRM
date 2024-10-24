package com.epam.gym.service;

import com.epam.gym.dto.UserDto;

import java.math.BigInteger;
import java.util.Optional;

public interface UserService {

    Optional<UserDto> findByUsername(String username);

    boolean isUsernameAndPasswordMatch(String username, String password);

    void changePassword(String username, String oldPassword, String newPassword);

    void changePassword(BigInteger id, String oldPassword, String newPassword);

    boolean updateProfile(BigInteger id, String firstName, String lastName, boolean isActive);

    boolean updateActiveState(BigInteger id, boolean isActive);

    void updateActiveState(String username, boolean isActive);

    UserDto save(String firstName, String lastName, String password, boolean isActive);
}