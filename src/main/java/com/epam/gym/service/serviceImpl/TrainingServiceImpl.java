package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.TrainingDto;
import com.epam.gym.dto.model.request.TrainingAddRequestModel;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingService;
import com.epam.gym.service.TrainingTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    @Override
    public Optional<TrainingDto> findById(BigInteger id) {
        return trainingRepository.findById(id).map(TrainingEntity::toDto);
    }

    @Override
    public boolean save(TrainingAddRequestModel model) {
        var trainee = traineeService.findByUsername(model.traineeUsername());
        var trainer = trainerService.findByUsername(model.trainerUsername());

        if (trainee.isEmpty() || trainer.isEmpty()) {
            return false;
        }

        traineeService.assignTrainer(trainee.get().getId(), trainer.get().getId());

        var trainingEntity = new TrainingEntity(
                null,
                model.trainingName(),
                model.date(),
                model.duration(),
                TraineeEntity.fromDto(trainee.get()),
                TrainerEntity.fromDto(trainer.get()),
                trainer.get().getSpecialization()
        );

        trainingRepository.save(trainingEntity);

        return true;
    }

    @Override
    public boolean assignTrainer(String traineeUsername, BigInteger trainerId, String name, String type, LocalDate date, int duration) {
        var t = trainingTypeService.getTrainingTypeName(type);
        var trainee = traineeService.findByUsername(traineeUsername);
        var trainer = trainerService.findById(trainerId);

        if (t.isEmpty() || trainee.isEmpty() || trainer.isEmpty()) {
            return false;
        }

        var trainingType = TrainingTypeEntity.fromDto(t.get());

        traineeService.assignTrainer(trainee.get().getId(), trainer.get().getId());

        var trainingEntity = new TrainingEntity(
                null,
                name,
                date,
                duration,
                TraineeEntity.fromDto(trainee.get()),
                TrainerEntity.fromDto(trainer.get()),
                trainingType
        );

        trainingRepository.save(trainingEntity);

        return true;
    }
}