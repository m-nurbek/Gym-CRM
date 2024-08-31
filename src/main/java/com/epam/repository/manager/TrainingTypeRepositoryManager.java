package com.epam.repository.manager;

import com.epam.entity.TrainingTypeEntity;
import com.epam.repository.TrainingTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class TrainingTypeRepositoryManager implements RepositoryManager {
    private final TrainingTypeRepository trainingTypeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File("trainingTypes.json");

    @Autowired
    public TrainingTypeRepositoryManager(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    @PostConstruct
    public void loadFromFile() {
        try {
            if (file.exists()) {
                List<TrainingTypeEntity> trainingTypeList = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, TrainingTypeEntity.class));
                trainingTypeRepository.saveAll(trainingTypeList);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load training types from file", e);
        }
    }

    @Override
    @PreDestroy
    public void saveToFile() {
        try {
            objectMapper.writeValue(file, trainingTypeRepository.findAll());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save training types to file", e);
        }
    }
}