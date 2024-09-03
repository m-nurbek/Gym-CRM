package com.epam.gym.repository;

import com.epam.gym.entity.TrainerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class TrainerRepository extends MapRepositoryImpl<TrainerEntity, BigInteger> {
    @Autowired
    public TrainerRepository(Storage<TrainerEntity, BigInteger> storage) {
        super(storage);
    }
}