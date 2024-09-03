package com.epam.gym.repository.manager;

import javax.annotation.PostConstruct;

public interface RepositoryManager {
    @PostConstruct
    void loadFromFile();
}