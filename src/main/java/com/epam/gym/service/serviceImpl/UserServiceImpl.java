package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        return userEntity.map(UserEntity::toDto);
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        return false;
    }

    @Override
    public UserDto updateProfile(UserDto updatedUser) {
        return null;
    }

    @Override
    public void updateActiveState(boolean isActive) {

    }
}