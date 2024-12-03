package com.epam.gym.service;

import com.epam.gym.dto.TrainerWorkloadRequest;

/**
 * @author Nurbek on 03.12.2024
 */
public interface TrainerService {
    void processWorkload(TrainerWorkloadRequest request);
}