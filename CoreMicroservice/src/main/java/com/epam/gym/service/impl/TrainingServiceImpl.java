package com.epam.gym.service.impl;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.dto.request.TrainingAddRequestDto;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.integration.TrainerReportFeignClient;
import com.epam.gym.integration.dto.ActionType;
import com.epam.gym.integration.dto.TrainerWorkloadRequest;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerReportFeignClient trainerReportFeignClient;

    @Override
    public void save(TrainingAddRequestDto model) {
        var trainee = traineeRepository.findByUser_Username(model.traineeUsername()).orElseThrow(BadRequestException::new);
        var trainer = trainerRepository.findByUser_Username(model.trainerUsername()).orElseThrow(BadRequestException::new);

        var trainingEntity = new TrainingEntity(
                null,
                model.trainingName(),
                model.date(),
                model.duration(),
                trainee,
                trainer,
                trainer.getSpecialization()
        );

        trainingRepository.saveAndFlush(trainingEntity);

        sendTrainerReport(trainer, model, ActionType.ADD);
    }

    private void sendTrainerReport(TrainerEntity trainer, TrainingAddRequestDto model, ActionType actionType) {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getIsActive(),
                model.date(),
                model.duration(),
                actionType
        );
        trainerReportFeignClient.handleTrainerWorkload(request);
    }
}