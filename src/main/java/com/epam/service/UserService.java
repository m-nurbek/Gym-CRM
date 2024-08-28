package com.epam.service;

import com.epam.entity.UserEntity;

import java.util.List;

public interface UserService {
    void addUser(UserEntity user);

    void updateUser(UserEntity user);

    void deleteUser(long id);

    UserEntity getUser(long id);

    List<UserEntity> getUsers();
}