package com.epam.gym.service.strategy;

import com.epam.gym.dto.ActionType;
import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.entity.TrainerWorkloadEntity;

/**
 * @author Nurbek on 14.12.2024
 */
public class TrainingAction {
    private TrainingActionStrategy strategy;

    public TrainingAction(ActionType actionType) {
        setStrategy(actionType);
    }

    public void setStrategy(ActionType actionType) {
        strategy = switch (actionType) {
            case ActionType.ADD -> addStrategy();
            case ActionType.DELETE -> deleteStrategy();
        };
    }

    private TrainingActionStrategy addStrategy() {
        return (trainerEntity, request) ->
                trainerEntity.addTraining(request.trainingDate(), request.duration());
    }

    private TrainingActionStrategy deleteStrategy() {
        return (trainerEntity, request) ->
                trainerEntity.removeTraining(request.trainingDate(), request.duration());
    }

    public void execute(TrainerWorkloadEntity trainerWorkloadEntity, TrainerWorkloadRequest request) {
        strategy.execute(trainerWorkloadEntity, request);
    }
}