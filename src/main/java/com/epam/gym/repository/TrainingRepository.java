package com.epam.gym.repository;

import com.epam.gym.entity.TrainingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class TrainingRepository extends MapRepositoryImpl<TrainingEntity, BigInteger> {
    @Autowired
    public TrainingRepository(Storage<TrainingEntity, BigInteger> storage) {
        super(storage);
    }
}