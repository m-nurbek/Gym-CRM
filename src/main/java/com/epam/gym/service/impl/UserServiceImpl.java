package com.epam.gym.service.impl;

import com.epam.gym.controller.exception.ConflictException;
import com.epam.gym.dto.UserDto;
import com.epam.gym.dto.UserRole;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.UserService;
import com.epam.gym.util.AtomicBigInteger;
import com.epam.gym.util.UserProfileUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username).map(x ->
                new UserDto(
                        x.getId(),
                        x.getFirstName(),
                        x.getLastName(),
                        x.getUsername(),
                        x.getPassword(),
                        x.getIsActive(),
                        getUserRoles(x)
                ));
    }

    @Override
    public boolean isUsernameAndPasswordMatch(String username, String password) {
        var user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return false;
        }

        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        var user = userRepository.findByUsername(username);
        return user.filter(userEntity -> changePasswordForUser(userEntity, oldPassword, newPassword)).isPresent();
    }

    @Override
    public boolean changePassword(BigInteger id, String oldPassword, String newPassword) {
        var user = userRepository.findById(id);
        return user.filter(userEntity -> changePasswordForUser(userEntity, oldPassword, newPassword)).isPresent();
    }

    private boolean changePasswordForUser(UserEntity user, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        userRepository.updatePasswordById(user.getId(), passwordEncoder.encode(newPassword));
        return true;
    }

    @Override
    public boolean updateProfile(BigInteger id, String firstName, String lastName, boolean isActive) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        userRepository.updateProfileById(id, firstName, lastName, isActive);
        return true;
    }

    @Override
    public boolean updateActiveState(BigInteger id, boolean isActive) {
        var u = userRepository.findById(id);

        if (u.isEmpty()) {
            return false;
        }

        userRepository.updateIsActiveById(u.get().getId(), isActive);
        return true;
    }

    @Override
    public boolean updateActiveState(String username, boolean isActive) {
        var u = userRepository.findByUsername(username).orElse(null);

        if (u == null) {
            return false;
        }

        userRepository.updateIsActiveById(u.getId(), isActive);
        return true;
    }

    @Override
    public UserDto save(String firstName, String lastName, String password, boolean isActive) {
        String uniqueUsername = generateUniqueUsername(firstName, lastName);
        String generatedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = new UserEntity(
                null,
                firstName,
                lastName,
                uniqueUsername,
                generatedPassword,
                isActive,
                null,
                null
        );

        if (!userEntity.isValid()) {
            throw new ConflictException("Invalid user");
        }

        UserEntity u = userRepository.save(userEntity);

        return new UserDto(
                u.getId(),
                u.getFirstName(),
                u.getLastName(),
                u.getUsername(),
                u.getPassword(),
                u.getIsActive(),
                getUserRoles(u)
        );
    }

    private synchronized String generateUniqueUsername(String firstName, String lastName) {
        AtomicBigInteger serialNumber = new AtomicBigInteger(BigInteger.valueOf(-1));
        String username;

        do {
            username = UserProfileUtil.generateUsername(firstName, lastName, serialNumber.incrementAndGet());
        } while (userRepository.findByUsername(username).isPresent());

        return username;
    }

    private List<UserRole> getUserRoles(UserEntity user) {
        if (user == null) {
            return List.of();
        }

        List<UserRole> roles = new ArrayList<>();

        if (user.getTrainer() != null && user.getTrainee() != null) {
            throw new ConflictException("User cannot be trainee and trainer at the same time");
        } else if (user.getTrainer() != null) {
            roles.add(UserRole.ROLE_TRAINER);
        } else {
            roles.add(UserRole.ROLE_TRAINEE);
        }

        return roles;
    }
}