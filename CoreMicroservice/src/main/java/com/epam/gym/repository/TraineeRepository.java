package com.epam.gym.repository;

import com.epam.gym.entity.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<TraineeEntity, BigInteger> {

    boolean existsByUser_Username(String username);

    Optional<TraineeEntity> findByUser_Username(String username);

    @Transactional
    @Modifying
    @Query("""
            update TraineeEntity t
            set t.dob = :dob,
            t.address = :address
            where t.id = :id""")
    void update(@Param("id") BigInteger id,
                @Param("dob") LocalDate dob,
                @Param("address") String address);
}