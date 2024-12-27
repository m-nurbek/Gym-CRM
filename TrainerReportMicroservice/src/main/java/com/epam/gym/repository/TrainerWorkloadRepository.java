package com.epam.gym.repository;

import com.epam.gym.entity.TrainerWorkloadEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Nurbek on 03.12.2024
 */
public interface TrainerWorkloadRepository extends MongoRepository<TrainerWorkloadEntity, String> {

    Optional<TrainerWorkloadEntity> findByUsername(String username);

    void deleteByUsernameIn(Collection<String> usernames);
}