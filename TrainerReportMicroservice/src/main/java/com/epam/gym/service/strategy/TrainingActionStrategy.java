package com.epam.gym.service.strategy;

import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.entity.TrainerWorkloadEntity;

/**
 * @author Nurbek on 14.12.2024
 */
@FunctionalInterface
public interface TrainingActionStrategy {
    void execute(TrainerWorkloadEntity trainerWorkloadEntity, TrainerWorkloadRequest request);
}