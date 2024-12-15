package com.epam.gym.repository;

import com.epam.gym.entity.TrainerWorkloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Nurbek on 03.12.2024
 */
public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkloadEntity, String> {

    Optional<TrainerWorkloadEntity> findByUsername(String username);

    List<TrainerWorkloadEntity> deleteByUsernameIn(Collection<String> usernames);
}