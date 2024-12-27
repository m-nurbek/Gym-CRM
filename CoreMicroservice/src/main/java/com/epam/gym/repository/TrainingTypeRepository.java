package com.epam.gym.repository;

import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.TrainingTypeEnum;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingTypeEntity, BigInteger> {

    Optional<TrainingTypeEntity> findByName(@NonNull TrainingTypeEnum name);
}