package com.epam.gym.service.impl;

import com.epam.gym.dto.ActionType;
import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.service.TrainerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nurbek on 03.12.2024
 */
@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public void processWorkload(TrainerWorkloadRequest request) {
        TrainerEntity trainer = trainerRepository.findByUsername(request.username())
                .orElseGet(() -> TrainerEntity.builder()
                        .username(request.username())
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .isActive(request.isActive())
                        .build());

        switch (request.actionType()) {
            case ActionType.ADD -> trainer.addTraining(request.trainingDate(), request.duration());
            case ActionType.DELETE -> trainer.removeTraining(request.trainingDate(), request.duration());
        }

        trainerRepository.save(trainer);
    }
}