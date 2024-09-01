package com.epam.service;

import com.epam.aop.Loggable;
import com.epam.dto.TrainerDto;
import com.epam.entity.TrainerEntity;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerService implements CrudService<TrainerDto, BigInteger> {
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, UserRepository userRepository, TrainingTypeRepository trainingTypeRepository) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    @Loggable
    public TrainerDto add(TrainerDto trainerDto) {
        TrainerEntity trainerEntity = TrainerEntity.fromDto(trainerDto);
        TrainerEntity t = trainerRepository.save(trainerEntity);

        return t.toDto(trainingTypeRepository, userRepository);
    }

    @Override
    @Loggable
    public TrainerDto update(TrainerDto trainerDto) {
        if (trainerDto.getId() == null) {
            return null;
        }

        TrainerEntity trainerEntity = TrainerEntity.fromDto(trainerDto);
        trainerRepository.update(trainerEntity.getId(), trainerEntity);

        return get(trainerEntity.getId()).orElse(null);
    }

    @Override
    @Loggable
    public void delete(BigInteger id) {
        trainerRepository.deleteById(id);
    }

    @Override
    @Loggable
    public Optional<TrainerDto> get(BigInteger id) {
        return trainerRepository.findById(id).map(trainerEntity -> trainerEntity.toDto(trainingTypeRepository, userRepository));
    }

    @Override
    @Loggable
    public List<TrainerDto> getAll() {
        List<TrainerEntity> trainerEntities = new ArrayList<>();
        trainerRepository.findAll().forEach(trainerEntities::add);

        return trainerEntities.stream()
                .map(trainerEntity -> trainerEntity.toDto(trainingTypeRepository, userRepository))
                .toList();
    }
}