package com.epam.gym.repository.hibernate;

import com.epam.gym.entity.TrainingEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TrainingRepository extends HibernateRepository<TrainingEntity, BigInteger> {
}