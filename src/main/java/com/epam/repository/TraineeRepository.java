package com.epam.repository;

import com.epam.entity.TraineeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class TraineeRepository extends MapRepositoryImpl<TraineeEntity, BigInteger> {
    @Autowired
    public TraineeRepository(Storage<TraineeEntity, BigInteger> storage) {
        super(storage);
    }
}