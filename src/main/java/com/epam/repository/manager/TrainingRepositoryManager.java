package com.epam.repository.manager;

import com.epam.entity.TrainingEntity;
import com.epam.repository.TrainingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class TrainingRepositoryManager implements RepositoryManager {
    private final TrainingRepository trainingRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File("trainings.json");

    @Autowired
    public TrainingRepositoryManager(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    @PostConstruct
    public void loadFromFile() {
        try {
            if (file.exists()) {
                List<TrainingEntity> trainingList = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, TrainingEntity.class));
                trainingRepository.saveAll(trainingList);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load trainings from file", e);
        }
    }

    @Override
    @PreDestroy
    public void saveToFile() {
        try {
            objectMapper.writeValue(file, trainingRepository.findAll());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save trainings to file", e);
        }
    }
}