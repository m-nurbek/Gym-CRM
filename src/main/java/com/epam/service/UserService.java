package com.epam.service;

import com.epam.aop.Loggable;
import com.epam.dto.UserDto;
import com.epam.entity.UserEntity;
import com.epam.repository.UserRepository;
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
        userRepository.save(userEntity);
    }

    @Override
    public void update(UserDto userDto) {
        UserEntity userEntity = UserEntity.fromDto(userDto);
        userRepository.update(userEntity.getId(), userEntity);
    }

    @Override
    public void delete(BigInteger id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserDto> get(BigInteger id) {
        return userRepository.findById(id).map(UserEntity::toDto);
    }

    @Override
    public List<UserDto> getAll() {
        List<UserEntity> userEntities = new ArrayList<>();
        userRepository.findAll().forEach(userEntities::add);

        return userEntities.stream()
                .map(UserEntity::toDto)
                .toList();
    }
}