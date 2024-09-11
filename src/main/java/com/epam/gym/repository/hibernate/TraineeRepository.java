package com.epam.gym.repository.hibernate;

import com.epam.gym.entity.TraineeEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TraineeRepository extends HibernateRepository<TraineeEntity, BigInteger> {
}