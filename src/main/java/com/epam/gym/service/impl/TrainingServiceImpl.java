package com.epam.gym.service.impl;

import com.epam.gym.dto.request.TrainingAddRequestDto;
import com.epam.gym.entity.TrainingEntity;
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

    @Override
    public boolean save(TrainingAddRequestDto model) {
        var trainee = traineeRepository.findByUser_Username(model.traineeUsername());
        var trainer = trainerRepository.findByUser_Username(model.trainerUsername());

        if (trainee.isEmpty() || trainer.isEmpty()) {
            return false;
        }

        var trainingEntity = new TrainingEntity(
                null,
                model.trainingName(),
                model.date(),
                model.duration(),
                trainee.get(),
                trainer.get(),
                trainer.get().getSpecialization()
        );

        trainingRepository.save(trainingEntity);

        return true;
    }
}