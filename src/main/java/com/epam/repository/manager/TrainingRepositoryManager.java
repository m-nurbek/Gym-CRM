package com.epam.repository.manager;

import com.epam.entity.TrainingEntity;
import com.epam.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class TrainingRepositoryManager extends RepositoryManagerImpl<TrainingEntity, BigInteger> {
    @Autowired
    public TrainingRepositoryManager(TrainingRepository trainingRepository, @Value("${app.file.trainings}") String source) {
        super(trainingRepository, source);
    }
}