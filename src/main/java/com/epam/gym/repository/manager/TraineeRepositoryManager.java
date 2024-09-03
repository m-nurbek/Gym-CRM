package com.epam.gym.repository.manager;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.repository.TraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class TraineeRepositoryManager extends RepositoryManagerImpl<TraineeEntity, BigInteger> {
    @Autowired
    public TraineeRepositoryManager(TraineeRepository traineeRepository, @Value("${app.file.trainees}") String source) {
        super(traineeRepository, source);
    }
}