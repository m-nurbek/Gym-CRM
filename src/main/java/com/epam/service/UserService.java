package com.epam.service;

import com.epam.aop.Loggable;
import com.epam.dto.UserDto;
import com.epam.entity.UserEntity;
import com.epam.repository.UserRepository;
import com.epam.util.UserProfileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements CrudService<UserDto, BigInteger> {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Loggable
    public void add(UserDto userDto) {
        UserEntity userEntity = UserEntity.fromDto(userDto);

        int serialNumber = 0;
        String username;

        do {
            username = UserProfileUtil.generateUsername(userEntity.getFirstName(), userEntity.getLastName(), serialNumber);
            serialNumber++;
        } while (userRepository.findByUsername(username).isPresent());

        userEntity.setUsername(username);
        userEntity.setPassword(UserProfileUtil.generatePassword());

        userRepository.save(userEntity);
    }

    @Override
    @Loggable
    public void update(UserDto userDto) {
        UserEntity userEntity = UserEntity.fromDto(userDto);
        userRepository.update(userEntity.getId(), userEntity);
    }

    @Override
    @Loggable
    public void delete(BigInteger id) {
        userRepository.deleteById(id);
    }

    @Override
    @Loggable
    public Optional<UserDto> get(BigInteger id) {
        return userRepository.findById(id).map(UserEntity::toDto);
    }

    @Override
    @Loggable
    public List<UserDto> getAll() {
        List<UserEntity> userEntities = new ArrayList<>();
        userRepository.findAll().forEach(userEntities::add);

        return userEntities.stream()
                .map(UserEntity::toDto)
                .toList();
    }
}