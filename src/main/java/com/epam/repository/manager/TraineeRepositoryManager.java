package com.epam.repository.manager;

import com.epam.entity.TraineeEntity;
import com.epam.repository.TraineeRepository;
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