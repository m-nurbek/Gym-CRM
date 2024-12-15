package com.epam.gym.service.impl;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.dto.request.TrainingAddRequestDto;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.integration.RabbitMQService;
import com.epam.gym.integration.dto.ActionType;
import com.epam.gym.integration.dto.TrainerWorkloadRequest;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.service.TrainingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final RabbitMQService rabbitMQService;

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

        trainee.getTrainers().add(trainer);
        trainingRepository.saveAndFlush(trainingEntity);
        sendTrainerReport(trainer, model);
    }

    private void sendTrainerReport(TrainerEntity trainer, TrainingAddRequestDto model) {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getIsActive(),
                model.date(),
                model.duration(),
                ActionType.ADD
        );

        log.trace(MarkerFactory.getMarker("REQUEST TO MICROSERVICE"),
                "Sending request to 'TrainerReport' microservice with request body: {}", request);
        rabbitMQService.sendReportRequest(request);
    }
}