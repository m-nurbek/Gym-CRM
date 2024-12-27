package com.epam.gym.repository;

import com.epam.gym.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Set;

public interface TrainingRepository extends JpaRepository<TrainingEntity, BigInteger> {
    @Query("select t from TrainingEntity t where t.trainee.user.username = :username")
    Set<TrainingEntity> findByTraineeUsername(@Param("username") String username);

    @Query("select t from TrainingEntity t where t.trainer.user.username = :username")
    Set<TrainingEntity> findByTrainerUsername(@Param("username") String username);

    void deleteTrainingEntitiesByTrainee_IdAndTrainer_Id(BigInteger traineeId, BigInteger trainerId);
}