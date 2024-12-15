package com.epam.gym.service;

import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.dto.WorkloadDeleteRequest;
import com.epam.gym.entity.TrainerWorkloadEntity;

/**
 * @author Nurbek on 03.12.2024
 */
public interface TrainerWorkloadService {
    void addWorkloadReport(TrainerWorkloadRequest request);

    void deleteWorkloadReport(WorkloadDeleteRequest trainerWorkloadRequest);

    default TrainerWorkloadEntity defineTrainer(TrainerWorkloadRequest request) {
        return TrainerWorkloadEntity.builder()
                .username(request.username())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .isActive(request.isActive())
                .build();
    }
}