package com.epam.gym.repository.hibernate;

import com.epam.gym.entity.TrainerEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TrainerRepository extends HibernateRepository<TrainerEntity, BigInteger> {
}