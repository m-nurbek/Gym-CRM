package com.epam.gym.repository;

import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.TrainingTypeEnum;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends HibernateRepository<TrainingTypeEntity, BigInteger> {

    Optional<TrainingTypeEntity> findByName(String name);

    Optional<TrainingTypeEntity> findByName(TrainingTypeEnum name);
}