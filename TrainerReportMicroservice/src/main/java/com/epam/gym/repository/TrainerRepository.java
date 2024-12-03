package com.epam.gym.repository;

import com.epam.gym.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Nurbek on 03.12.2024
 */
public interface TrainerRepository extends JpaRepository<TrainerEntity, String> {

    Optional<TrainerEntity> findByUsername(String username);
}