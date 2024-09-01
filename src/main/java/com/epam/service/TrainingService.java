package com.epam.service;

import com.epam.dto.TrainingDto;
import com.epam.entity.TrainingEntity;
import com.epam.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingService implements CrudService<TrainingDto, BigInteger> {
    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, UserRepository userRepository, TrainingTypeRepository trainingTypeRepository, TrainerRepository trainerRepository, TraineeRepository traineeRepository) {
        this.trainingRepository = trainingRepository;
        this.userRepository = userRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
    }

    @Override
    public TrainingDto add(TrainingDto obj) {
        TrainingEntity trainingEntity = TrainingEntity.fromDto(obj);
        TrainingEntity t = trainingRepository.save(trainingEntity);

        return t.toDto(trainerRepository, traineeRepository, userRepository, trainingTypeRepository);
    }

    @Override
    public TrainingDto update(TrainingDto trainingDto) {
        if (trainingDto.getId() == null) {
            return null;
        }

        TrainingEntity trainingEntity = TrainingEntity.fromDto(trainingDto);
        trainingRepository.update(trainingEntity.getId(), trainingEntity);

        return get(trainingEntity.getId()).orElse(null);
    }

    @Override
    public void delete(BigInteger bigInteger) {
        trainingRepository.deleteById(bigInteger);
    }

    @Override
    public Optional<TrainingDto> get(BigInteger bigInteger) {
        return trainingRepository.findById(bigInteger).map(trainingEntity -> trainingEntity.toDto(trainerRepository, traineeRepository, userRepository, trainingTypeRepository));
    }

    @Override
    public List<TrainingDto> getAll() {
        List<TrainingEntity> trainingEntities = new ArrayList<>();
        trainingRepository.findAll().forEach(trainingEntities::add);

        return trainingEntities.stream()
                .map(trainingEntity -> trainingEntity.toDto(trainerRepository, traineeRepository, userRepository, trainingTypeRepository))
                .toList();
    }
}