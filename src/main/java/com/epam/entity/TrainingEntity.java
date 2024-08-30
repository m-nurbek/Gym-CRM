package com.epam.entity;

import com.epam.dto.TraineeDto;
import com.epam.dto.TrainerDto;
import com.epam.dto.TrainingDto;
import com.epam.dto.TrainingTypeDto;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TrainingEntity implements Entity<BigInteger> {
    private BigInteger id;
    @NonNull
    private BigInteger traineeId;
    @NonNull
    private BigInteger trainerId;
    @NonNull
    private String name;
    @NonNull
    private BigInteger type; // training type
    @NonNull
    private Date date;
    @NonNull
    private String duration;

    public TrainingDto toDto(TrainerRepository trainerRepository, TraineeRepository traineeRepository, UserRepository userRepository, TrainingTypeRepository trainingTypeRepository) {
        Optional<TrainerEntity> trainer = trainerRepository.findById(trainerId);
        Optional<TraineeEntity> trainee = traineeRepository.findById(traineeId);
        Optional<TrainingTypeEntity> trainingType = trainingTypeRepository.findById(type);

        TrainerDto trainerDto = null;
        TraineeDto traineeDto = null;
        TrainingTypeDto trainingTypeDto = null;

        if (trainer.isPresent()) {
            trainerDto = trainer.get().toDto(trainingTypeRepository, userRepository);
        }

        if (trainee.isPresent()) {
            traineeDto = trainee.get().toDto(userRepository);
        }

        if (trainingType.isPresent()) {
            trainingTypeDto = trainingType.get().toDto();
        }

        return new TrainingDto(id, traineeDto, trainerDto, name, trainingTypeDto, date, duration);
    }

    public static TrainingEntity fromDto(TrainingDto trainingDto) {
        return new TrainingEntity(trainingDto.getId(), trainingDto.getTrainee().getId(), trainingDto.getTrainer().getId(), trainingDto.getName(), trainingDto.getType().getId(), trainingDto.getDate(), trainingDto.getDuration());
    }
}