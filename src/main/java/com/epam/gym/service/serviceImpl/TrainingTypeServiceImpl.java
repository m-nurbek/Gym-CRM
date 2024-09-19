package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.repository.TrainingTypeRepository;
import com.epam.gym.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public TrainingTypeDto save(TrainingTypeDto type) {
        var typeEntity = trainingTypeRepository.save(TrainingTypeEntity.fromDto(type));
        return typeEntity.toDto();
    }

    @Override
    public boolean update(TrainingTypeDto type) {
        Optional<TrainingTypeEntity> trainingTypeEntityOptional = trainingTypeRepository.findById(type.getId());

        if (trainingTypeEntityOptional.isPresent()) {
            TrainingTypeEntity trainingTypeEntity = trainingTypeEntityOptional.get();
            trainingTypeEntity.setName(type.getName());
            trainingTypeEntity.setTrainings(type.getTrainings());
            trainingTypeEntity.setTrainers(type.getTrainers());

            return trainingTypeRepository.update(trainingTypeEntity);
        }

        return false;
    }

    @Override
    public boolean delete(BigInteger id) {
        return trainingTypeRepository.deleteById(id);
    }

    @Override
    public Optional<TrainingTypeDto> get(BigInteger id) {
        var type = trainingTypeRepository.findById(id);
        return type.map(TrainingTypeEntity::toDto);
    }

    @Override
    public List<TrainingTypeDto> getAll() {
        List<TrainingTypeEntity> trainingTypeEntities = new ArrayList<>();
        trainingTypeRepository.findAll().forEach(trainingTypeEntities::add);

        return trainingTypeEntities.stream().map(TrainingTypeEntity::toDto).toList();
    }

    @Override
    public long count() {
        return trainingTypeRepository.count();
    }

    @Override
    public Optional<TrainingTypeDto> getTrainingTypeName(String name) {
        var type = trainingTypeRepository.findByName(name);
        return type.map(TrainingTypeEntity::toDto);
    }
}