package com.epam.gym.repository;

import com.epam.gym.entity.TrainingTypeEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends HibernateRepository<TrainingTypeEntity, BigInteger> {

    Optional<TrainingTypeEntity> findByName(String name);
}