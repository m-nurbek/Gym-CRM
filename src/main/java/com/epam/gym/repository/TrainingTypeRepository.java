package com.epam.gym.repository;

import com.epam.gym.entity.TrainingTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class TrainingTypeRepository extends MapRepositoryImpl<TrainingTypeEntity, BigInteger> {
    @Autowired
    public TrainingTypeRepository(Storage<TrainingTypeEntity, BigInteger> storage) {
        super(storage);
    }
}