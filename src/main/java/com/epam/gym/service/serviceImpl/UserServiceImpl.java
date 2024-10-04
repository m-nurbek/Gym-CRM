package com.epam.gym.service.serviceImpl;

import com.epam.gym.controller.exception.ConflictException;
import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.UserService;
import com.epam.gym.util.AtomicBigInteger;
import com.epam.gym.util.UserProfileUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<UserDto> findByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        return userEntity.map(UserEntity::toDto);
    }

    @Override
    public boolean isUsernameAndPasswordMatch(String username, String password) {
        return userRepository.existsByUsernameAndPassword(username, password);
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
        if (!user.getPassword().equals(oldPassword)) {
            return false;
        }

        userRepository.updatePasswordById(user.getId(), newPassword);
        return true;
    }

    @Override
    public boolean updateProfile(UserDto updatedUser) {
        if (!userRepository.existsById(updatedUser.getId())) {
            return false;
        }

        userRepository.updateProfileById(updatedUser.getId(), updatedUser.getFirstName(), updatedUser.getLastName());
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
        var u = findByUsername(username);

        if (u.isEmpty()) {
            return false;
        }

        userRepository.updateIsActiveById(u.get().getId(), isActive);
        return true;
    }

    @Override
    public UserDto save(UserDto userDto) throws ConflictException {
        userDto.setUsername(generateUniqueUsername(userDto));
        userDto.setPassword(UserProfileUtil.generatePassword());

        UserEntity userEntity = UserEntity.fromDto(userDto);

        if (!userEntity.isValid()) {
            throw new ConflictException("Invalid user");
        }

        userEntity = userRepository.save(userEntity);
        return userEntity.toDto();
    }

    private synchronized String generateUniqueUsername(UserDto user) {
        AtomicBigInteger serialNumber = new AtomicBigInteger(BigInteger.valueOf(-1));
        String username;

        do {
            username = UserProfileUtil.generateUsername(user.getFirstName(), user.getLastName(), serialNumber.incrementAndGet());
        } while (userRepository.findByUsername(username).isPresent());

        return username;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserEntity::toDto).toList();
    }
}