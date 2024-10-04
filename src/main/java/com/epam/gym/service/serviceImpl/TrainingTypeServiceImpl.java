package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.dto.model.response.TrainingTypeResponseModel;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.repository.TrainingTypeRepository;
import com.epam.gym.service.TrainingTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public List<TrainingTypeDto> getAll() {
        return trainingTypeRepository.findAll().stream().map(TrainingTypeEntity::toDto).toList();
    }

    @Override
    public Set<TrainingTypeResponseModel> getAllToResponse() {
        return getAll().stream().map(
                type -> new TrainingTypeResponseModel(
                        type.getName().name(),
                        type.getId()
                )
        ).collect(Collectors.toSet());
    }

    @Override
    public Optional<TrainingTypeDto> getTrainingTypeName(String name) {
        var type = trainingTypeRepository.findByName(TrainingTypeEnum.valueOf(name.toUpperCase().trim()));
        return type.map(TrainingTypeEntity::toDto);
    }

    @Override
    public Optional<TrainingTypeDto> getTrainingTypeName(TrainingTypeEnum name) {
        var type = trainingTypeRepository.findByName(name);
        return type.map(TrainingTypeEntity::toDto);
    }
}