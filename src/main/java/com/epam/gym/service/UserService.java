package com.epam.gym.service;

import com.epam.gym.dto.UserDto;

import java.math.BigInteger;

public interface UserService {

    boolean isUsernameAndPasswordMatch(String username, String password);

    boolean changePassword(String username, String oldPassword, String newPassword);

    boolean changePassword(BigInteger id, String oldPassword, String newPassword);

    boolean updateProfile(BigInteger id, String firstName, String lastName, boolean isActive);

    boolean updateActiveState(BigInteger id, boolean isActive);

    boolean updateActiveState(String username, boolean isActive);

    UserDto save(String firstName, String lastName, boolean isActive);
}