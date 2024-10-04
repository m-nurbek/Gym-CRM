package com.epam.gym.repository;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;

@Repository
public interface TraineeRepository extends JpaRepository<TraineeEntity, BigInteger> {

    @Query(value = """
            from TrainerEntity as trainer
            where trainer not in (
                select trainee.trainers from TraineeEntity trainee, UserEntity user
                where user.username = :username AND user = trainee.user)
            """)
    Set<TrainerEntity> getUnassignedTrainersByUsername(@NonNull String username);

    @Transactional
    @Modifying
    @Query("""
            update TraineeEntity t
            set t.dob = :dob,
            t.address = :address,
            t.user = :user
            where t.id = :id""")
    void update(@Param("id") BigInteger id,
                @Param("dob") LocalDate dob,
                @Param("address") String address,
                @Param("user") UserEntity user);
}