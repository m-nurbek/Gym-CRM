package com.epam.gym.repository;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Set;

@Repository
public interface TraineeRepository extends HibernateRepository<TraineeEntity, BigInteger> {

    Set<TrainerEntity> getUnassignedTrainersByUsername(String username);
}