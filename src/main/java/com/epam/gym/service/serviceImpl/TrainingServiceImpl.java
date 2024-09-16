package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.TrainingDto;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
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
}