package com.epam.repository.manager;

import com.epam.entity.TrainerEntity;
import com.epam.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class TrainerRepositoryManager extends RepositoryManagerImpl<TrainerEntity, BigInteger> {
    @Autowired
    public TrainerRepositoryManager(TrainerRepository trainerRepository, @Value("${app.file.trainers}") String source) {
        super(trainerRepository, source);
    }
}