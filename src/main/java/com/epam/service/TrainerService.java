package com.epam.service;

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
    public void add(TrainerDto trainerDto) {
        TrainerEntity trainerEntity = TrainerEntity.fromDto(trainerDto);
        trainerRepository.save(trainerEntity);
    }

    @Override
    public void update(TrainerDto trainerDto) {
        TrainerEntity trainerEntity = TrainerEntity.fromDto(trainerDto);
        trainerRepository.update(trainerEntity.getId(), trainerEntity);
    }

    @Override
    public void delete(BigInteger id) {
        trainerRepository.deleteById(id);
    }

    @Override
    public Optional<TrainerDto> get(BigInteger id) {
        return trainerRepository.findById(id).map(trainerEntity -> trainerEntity.toDto(trainingTypeRepository, userRepository));
    }

    @Override
    public List<TrainerDto> getAll() {
        List<TrainerEntity> trainerEntities = new ArrayList<>();
        trainerRepository.findAll().forEach(trainerEntities::add);

        return trainerEntities.stream()
                .map(trainerEntity -> trainerEntity.toDto(trainingTypeRepository, userRepository))
                .toList();
    }
}