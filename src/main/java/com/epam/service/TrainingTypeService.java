package com.epam.service;

import com.epam.dto.TrainingTypeDto;
import com.epam.entity.TrainingTypeEntity;
import com.epam.repository.TrainingTypeRepository;
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
    public void add(TrainingTypeDto obj) {
        TrainingTypeEntity trainingTypeEntity = TrainingTypeEntity.fromDto(obj);
        trainingTypeRepository.save(trainingTypeEntity);
    }

    @Override
    public void update(TrainingTypeDto obj) {
        TrainingTypeEntity trainingTypeEntity = TrainingTypeEntity.fromDto(obj);
        trainingTypeRepository.update(trainingTypeEntity.getId(), trainingTypeEntity);
    }

    @Override
    public void delete(BigInteger bigInteger) {
        trainingTypeRepository.deleteById(bigInteger);
    }

    @Override
    public Optional<TrainingTypeDto> get(BigInteger bigInteger) {
        return trainingTypeRepository.findById(bigInteger).map(TrainingTypeEntity::toDto);
    }

    @Override
    public List<TrainingTypeDto> getAll() {
        List<TrainingTypeEntity> trainingTypeEntities = new ArrayList<>();
        trainingTypeRepository.findAll().forEach(trainingTypeEntities::add);

        return trainingTypeEntities.stream()
                .map(TrainingTypeEntity::toDto)
                .toList();
    }
}