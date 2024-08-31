package com.epam.repository.manager;

import com.epam.entity.TraineeEntity;
import com.epam.repository.TraineeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class TraineeRepositoryManager implements RepositoryManager {
    private final TraineeRepository traineeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File("trainees.json");

    @Autowired
    public TraineeRepositoryManager(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    @PostConstruct
    public void loadFromFile() {
        try {
            if (file.exists()) {
                List<TraineeEntity> traineeList = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, TraineeEntity.class));
                traineeRepository.saveAll(traineeList);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load trainees from file", e);
        }
    }

    @Override
    @PreDestroy
    public void saveToFile() {
        try {
            objectMapper.writeValue(file, traineeRepository.findAll());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save trainees to file", e);
        }
    }
}