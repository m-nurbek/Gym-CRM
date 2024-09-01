package com.epam.repository;

import com.epam.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public class UserRepository extends MapRepositoryImpl<UserEntity, BigInteger> {
    @Autowired
    public UserRepository(Storage<UserEntity, BigInteger> storage) {
        super(storage);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return map.values().stream().filter(
                userEntity -> userEntity.getUsername().equals(username)).findFirst();
    }
}