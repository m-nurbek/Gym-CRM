package com.epam.gym.service;

import com.epam.gym.aop.Loggable;
import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.repository.TrainingTypeRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingTypeService implements CrudService<TrainingTypeDto, BigInteger> {
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    @Loggable
    public TrainingTypeDto add(TrainingTypeDto obj) {
        TrainingTypeEntity trainingTypeEntity = TrainingTypeEntity.fromDto(obj);
        TrainingTypeEntity t = trainingTypeRepository.save(trainingTypeEntity);

        return t.toDto();
    }

    @Override
    @Loggable
    public TrainingTypeDto update(TrainingTypeDto obj) {
        if (obj == null || obj.getId() == null) {
            return null;
        }

        TrainingTypeEntity trainingTypeEntity = TrainingTypeEntity.fromDto(obj);
        trainingTypeRepository.update(trainingTypeEntity.getId(), trainingTypeEntity);

        return get(trainingTypeEntity.getId()).orElse(null);
    }

    @Override
    @Loggable
    public void delete(BigInteger bigInteger) {
        trainingTypeRepository.deleteById(bigInteger);
    }

    @Override
    @Loggable
    public Optional<TrainingTypeDto> get(BigInteger bigInteger) {
        return trainingTypeRepository.findById(bigInteger).map(TrainingTypeEntity::toDto);
    }

    @Override
    @Loggable
    public List<TrainingTypeDto> getAll() {
        List<TrainingTypeEntity> trainingTypeEntities = new ArrayList<>();
        trainingTypeRepository.findAll().forEach(trainingTypeEntities::add);

        return trainingTypeEntities.stream()
                .map(TrainingTypeEntity::toDto)
                .toList();
    }
}