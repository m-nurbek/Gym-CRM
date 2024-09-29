package com.epam.gym.service.serviceImpl;

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
import java.util.ArrayList;
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
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        var user = userRepository.findByUsername(username);
        return changePasswordForUser(user, oldPassword, newPassword);
    }

    @Override
    public boolean changePassword(BigInteger id, String oldPassword, String newPassword) {
        var user = userRepository.findById(id);
        return changePasswordForUser(user, oldPassword, newPassword);
    }

    private boolean changePasswordForUser(Optional<UserEntity> user, String oldPassword, String newPassword) {
        if (user.isEmpty() || !user.get().getPassword().equals(oldPassword)) {
            return false;
        }

        return userRepository.updatePassword(user.get().getId(), newPassword);
    }

    @Override
    public boolean updateProfile(UserDto updatedUser) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(updatedUser.getId());

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            userEntity.setUsername(updatedUser.getUsername());
            userEntity.setFirstName(updatedUser.getFirstName());
            userEntity.setLastName(updatedUser.getLastName());
            userEntity.setUsername(updatedUser.getUsername());

            return userRepository.update(userEntity);
        }

        return false;
    }

    @Override
    public boolean updateActiveState(BigInteger id, boolean isActive) {
        return userRepository.updateActiveState(id, isActive);
    }

    @Override
    public boolean updateActiveState(String username, boolean isActive) {
        var u = findByUsername(username);

        if (u.isEmpty()) {
            return false;
        }

        return updateActiveState(u.get().getId(), isActive);
    }

    @Override
    public UserDto save(UserDto userDto) {
        userDto.setUsername(generateUniqueUsername(userDto));
        userDto.setPassword(UserProfileUtil.generatePassword());

        var userEntity = userRepository.save(UserEntity.fromDto(userDto));
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
    public boolean update(UserDto updatedUser) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(updatedUser.getId());

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            userEntity.setUsername(updatedUser.getUsername());
            userEntity.setFirstName(updatedUser.getFirstName());
            userEntity.setLastName(updatedUser.getLastName());
            userEntity.setUsername(updatedUser.getUsername());
            userEntity.setIsActive(updatedUser.getIsActive());
            userEntity.setTrainer(updatedUser.getTrainer());
            userEntity.setTrainee(updatedUser.getTrainee());

            return userRepository.update(userEntity);
        }

        return false;
    }

    @Override
    public boolean delete(BigInteger id) {
        return userRepository.deleteById(id);
    }

    @Override
    public Optional<UserDto> get(BigInteger id) {
        var u = userRepository.findById(id);
        return u.map(UserEntity::toDto);
    }

    @Override
    public List<UserDto> getAll() {
        List<UserEntity> userEntities = new ArrayList<>();
        userRepository.findAll().forEach(userEntities::add);

        return userEntities.stream().map(UserEntity::toDto).toList();
    }

    @Override
    public long count() {
        return userRepository.count();
    }
}