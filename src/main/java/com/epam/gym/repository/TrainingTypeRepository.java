package com.epam.gym.repository;

import com.epam.gym.entity.TrainingTypeEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TrainingTypeRepository extends HibernateRepository<TrainingTypeEntity, BigInteger> {
}