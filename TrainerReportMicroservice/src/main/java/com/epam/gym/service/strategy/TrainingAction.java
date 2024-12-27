package com.epam.gym.service.strategy;

import com.epam.gym.dto.ActionType;
import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.entity.TrainerWorkloadEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nurbek on 14.12.2024
 */
@Slf4j
public class TrainingAction {
    private TrainingActionStrategy strategy;

    public TrainingAction(ActionType actionType) {
        setStrategy(actionType);
    }

    private void setStrategy(ActionType actionType) {
        strategy = switch (actionType) {
            case ActionType.ADD -> addStrategy();
            case ActionType.DELETE -> deleteStrategy();
            case null -> throw new IllegalArgumentException("actionType cannot be null");
        };
    }

    private TrainingActionStrategy addStrategy() {
        return (trainerEntity, request) -> {
            try {
                trainerEntity.addTraining(request.trainingDate(), request.duration());
            } catch (Exception ex) {
                log.error("Failed to add training from request: {}", request);
                throw new RuntimeException("Failed to add training", ex);
            }
        };
    }

    private TrainingActionStrategy deleteStrategy() {
        return (trainerEntity, request) -> {
            try {
                trainerEntity.removeTraining(request.trainingDate(), request.duration());
            } catch (Exception ex) {
                log.error("Failed to delete training from request: {}", request);
                throw new RuntimeException("Failed to delete training", ex);
            }
        };
    }

    public void execute(TrainerWorkloadEntity trainerWorkloadEntity, TrainerWorkloadRequest request) {
        strategy.execute(trainerWorkloadEntity, request);
    }
}