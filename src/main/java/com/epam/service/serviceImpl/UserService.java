package com.epam.service.serviceImpl;

import com.epam.dto.UserDto;
import com.epam.repository.UserRepository;
import com.epam.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements CrudService<UserDto, Long> {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(UserDto userDto) {
//        userRepository.save(userDto.getId(), userDto);

    }

    @Override
    public void update(UserDto obj) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public UserDto get(Long aLong) {
        return null;
    }

    @Override
    public List<UserDto> getAll() {
        return List.of();
    }
}