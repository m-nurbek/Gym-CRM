package com.epam.gym.repository;

import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, BigInteger> {

    boolean existsByUser_Username(String username);

    Optional<TrainerEntity> findByUser_Username(String username);

    @Transactional
    @Modifying
    @Query("update TrainerEntity t set t.specialization = :specialization where t.id = :id")
    void updateSpecialization(@Param("id") BigInteger id, @Param("specialization") TrainingTypeEntity specialization);

    @Transactional
    @Modifying
    @Query("update TrainerEntity t set t.user = :user, t.specialization = :specialization where t.id = :id")
    void update(@Param("id") BigInteger id,
                @Param("user") UserEntity user,
                @Param("specialization") TrainingTypeEntity specialization);

    @Query(value = """
            from TrainerEntity as trainer
            where trainer not in (
                select trainee.trainers from TraineeEntity trainee, UserEntity user
                where user.username = :username AND user = trainee.user)
            """)
    Set<TrainerEntity> getUnassignedTrainersByTraineeUsername(@NonNull String username);
}