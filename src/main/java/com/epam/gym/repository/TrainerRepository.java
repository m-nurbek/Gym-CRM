package com.epam.gym.repository;

import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, BigInteger> {
    @Transactional
    @Modifying
    @Query("update TrainerEntity t set t.user = :user, t.specialization = :specialization where t.id = :id")
    void update(@Param("id") BigInteger id,
                @Param("user") UserEntity user,
                @Param("specialization") TrainingTypeEntity specialization);
}