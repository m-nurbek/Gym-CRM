package com.epam.service.serviceImpl;

import com.epam.entity.UserEntity;
import com.epam.repository.UserRepository;
import com.epam.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(UserEntity user) {
        userRepository.save(user.getId(), user);
    }

    @Override
    public void updateUser(UserEntity user) {
        userRepository.update(user.getId(), user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity getUser(long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public List<UserEntity> getUsers() {
        Iterable<UserEntity> userEntityIterable = userRepository.findAll();
        List<UserEntity> list = new ArrayList<>();

        userEntityIterable.forEach(list::add);

        return list;
    }
}