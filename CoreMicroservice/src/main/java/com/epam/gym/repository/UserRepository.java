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
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, BigInteger> {
    @Transactional
    @Modifying
    @Query("""
            update UserEntity u
            set u.firstName = :firstName,
            u.lastName = :lastName,
            u.isActive = :isActive
            where u.id = :id""")
    void updateProfileById(@Param("id") @NonNull BigInteger id,
                           @Param("firstName") @NonNull String firstName,
                           @Param("lastName") @NonNull String lastName,
                           @Param("isActive") @NonNull Boolean isActive);

    Optional<UserEntity> findByUsername(@NonNull String username);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.isActive = :isActive where u.id = :id")
    void updateIsActiveById(@Param("id") @NonNull BigInteger id, @Param("isActive") @NonNull Boolean isActive);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.password = :password where u.id = :id")
    void updatePasswordById(@Param("id") @NonNull BigInteger id, @Param("password") @NonNull String password);

    boolean existsByUsernameAndPassword(@NonNull String username, @NonNull String password);

    @Transactional
    @Modifying
    @Query("""
            update UserEntity u
            set u.firstName = :firstName,
            u.lastName = :lastName,
            u.username = :username,
            u.isActive = :isActive,
            u.trainee = :trainee,
            u.trainer = :trainer
            where u.id = :id""")
    void update(@Param("id") BigInteger id,
                @Param("firstName") String firstName,
                @Param("lastName") String lastName,
                @Param("username") String username,
                @Param("isActive") Boolean isActive,
                @Param("trainee") TraineeEntity trainee,
                @Param("trainer") TrainerEntity trainer);
}