package com.epam.gym.repository.manager;

import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class TrainingTypeRepositoryManager extends RepositoryManagerImpl<TrainingTypeEntity, BigInteger> {
    @Autowired
    public TrainingTypeRepositoryManager(TrainingTypeRepository trainingTypeRepository, @Value("${app.file.training-types}") String source) {
        super(trainingTypeRepository, source);
    }
}