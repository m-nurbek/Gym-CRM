package com.epam.repository.manager;

import com.epam.entity.UserEntity;
import com.epam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class UserRepositoryManager extends RepositoryManagerImpl<UserEntity, BigInteger> {
    @Autowired
    public UserRepositoryManager(UserRepository userRepository, @Value("${app.file.users}") String source) {
        super(userRepository, source);
    }
}