package com.epam.gym.service;

import com.epam.gym.dto.UserDto;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface UserService extends CrudService<UserDto, BigInteger> {

    Optional<UserDto> findByUsername(String username);

    boolean isUsernameAndPasswordMatch(String username, String password);

    boolean isTrainee(String username);

    boolean isTrainer(String username);

    boolean isTrainee(BigInteger id);

    boolean isTrainer(BigInteger id);

    boolean changePassword(String username, String oldPassword, String newPassword);

    boolean changePassword(BigInteger id, String oldPassword, String newPassword);

    boolean updateProfile(UserDto updatedUser);

    boolean updateActiveState(BigInteger id, boolean isActive);

    boolean updateActiveState(String username, boolean isActive);

    UserDto save(UserDto userDto);

    List<UserDto> getAll();
}