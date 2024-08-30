package com.epam.repository;

import com.epam.entity.TrainingTypeEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class TrainingTypeRepository extends MapRepositoryImpl<TrainingTypeEntity, BigInteger> {
}