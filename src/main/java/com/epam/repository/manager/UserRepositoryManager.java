package com.epam.repository.manager;

import com.epam.entity.UserEntity;
import com.epam.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class UserRepositoryManager implements RepositoryManager {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File("trainingTypes.json");

    @Autowired
    public UserRepositoryManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @PostConstruct
    public void loadFromFile() {
        try {
            if (file.exists()) {
                List<UserEntity> userList = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, UserEntity.class));
                userRepository.saveAll(userList);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load users from file", e);
        }
    }

    @Override
    @PreDestroy
    public void saveToFile() {
        try {
            objectMapper.writeValue(file, userRepository.findAll());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save users to file", e);
        }
    }
}