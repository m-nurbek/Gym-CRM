package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.TrainingDto;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingService;
import com.epam.gym.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeService traineeService, TrainerService trainerService, TrainingTypeService trainingTypeService) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    public TrainingDto save(TrainingDto training) {
        var trainingEntity = trainingRepository.save(TrainingEntity.fromDto(training));
        return trainingEntity.toDto();
    }

    @Override
    public boolean update(TrainingDto training) {
        Optional<TrainingEntity> trainingEntityOptional = trainingRepository.findById(training.getId());

        if (trainingEntityOptional.isPresent()) {
            TrainingEntity trainingEntity = trainingEntityOptional.get();
            trainingEntity.setDate(training.getDate());
            trainingEntity.setName(training.getName());
            trainingEntity.setDuration(training.getDuration());
            trainingEntity.setType(training.getType());
            trainingEntity.setTrainee(training.getTrainee());
            trainingEntity.setTrainer(training.getTrainer());

            return trainingRepository.update(trainingEntity);
        }

        return false;
    }

    @Override
    public boolean delete(BigInteger id) {
        return trainingRepository.deleteById(id);
    }

    @Override
    public Optional<TrainingDto> get(BigInteger id) {
        var training = trainingRepository.findById(id);
        return training.map(TrainingEntity::toDto);
    }

    @Override
    public List<TrainingDto> getAll() {
        List<TrainingEntity> trainingEntities = new ArrayList<>();
        trainingRepository.findAll().forEach(trainingEntities::add);

        return trainingEntities.stream().map(TrainingEntity::toDto).toList();
    }

    @Override
    public long count() {
        return trainingRepository.count();
    }

    @Override
    public boolean assignTrainer(String traineeUsername, BigInteger trainerId, String name, String type, LocalDate date, int duration) {
        var t = trainingTypeService.getTrainingTypeName(type);
        var trainee = traineeService.findByUsername(traineeUsername);
        var trainer = trainerService.get(trainerId);

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

    @Override
    public boolean unassignTrainer(String traineeUsername, BigInteger trainerId) {
        var trainee = traineeService.findByUsername(traineeUsername);
        var trainer = trainerService.get(trainerId);

        if (trainee.isEmpty() || trainer.isEmpty()) {
            return false;
        }

        var traineeEntity = TraineeEntity.fromDto(trainee.get());
        var trainerEntity = TrainerEntity.fromDto(trainer.get());

        var trainingEntity = trainingRepository.findByTraineeAndTrainer(traineeEntity, trainerEntity);

        if (trainingEntity.isEmpty()) {
            return false;
        }

        trainingRepository.deleteById(trainingEntity.get().getId());
        traineeService.unassignTrainer(traineeEntity.getId(), trainerEntity.getId());

        return true;
    }

    @Override
    public boolean cancelTraining(BigInteger trainingId) {
        return trainingRepository.deleteById(trainingId);
    }
}