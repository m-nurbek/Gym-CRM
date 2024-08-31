package com.epam.repository.manager;

import com.epam.entity.TrainerEntity;
import com.epam.repository.TrainerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class TrainerRepositoryManager implements RepositoryManager {
    private final TrainerRepository trainerRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File("trainers.json");

    @Autowired
    public TrainerRepositoryManager(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    @PostConstruct
    public void loadFromFile() {
        try {
            if (file.exists()) {
                List<TrainerEntity> trainerList = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, TrainerEntity.class));
                trainerRepository.saveAll(trainerList);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load trainers from file", e);
        }
    }

    @Override
    @PreDestroy
    public void saveToFile() {
        try {
            objectMapper.writeValue(file, trainerRepository.findAll());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save trainers to file", e);
        }
    }
}