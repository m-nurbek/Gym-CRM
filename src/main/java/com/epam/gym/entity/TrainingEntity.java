package com.epam.gym.entity;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.TrainingDto;
import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.TrainingTypeRepository;
import com.epam.gym.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TRAINING")
public class TrainingEntity implements EntityInterface<BigInteger> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private BigInteger id;
    @NonNull
    @Column(name = "TRAINEE_ID")
    private BigInteger traineeId;
    @NonNull
    @Column(name = "TRAINER_ID")
    private BigInteger trainerId;
    @NonNull
    @Column(name = "NAME")
    private String name;
    @NonNull
    @Column(name = "TYPE")
    private BigInteger type; // training type
    @NonNull
    @Column(name = "DATE")
    private Date date;
    @NonNull
    @Column(name = "DURATION")
    private String duration;

    @JsonCreator
    public TrainingEntity(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("traineeId") BigInteger traineeId,
            @JsonProperty("trainerId") BigInteger trainerId,
            @JsonProperty("name") String name,
            @JsonProperty("type") BigInteger type,
            @JsonProperty("date") Date date,
            @JsonProperty("duration") String duration
    ) {
        this.id = id;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public TrainingDto toDto(TrainerRepository trainerRepository, TraineeRepository traineeRepository, UserRepository userRepository, TrainingTypeRepository trainingTypeRepository) {
        AtomicReference<TrainerDto> trainerDto = new AtomicReference<>();
        AtomicReference<TraineeDto> traineeDto = new AtomicReference<>();
        AtomicReference<TrainingTypeDto> trainingTypeDto = new AtomicReference<>();

        trainerRepository.findById(trainerId).ifPresent(t -> trainerDto.set(t.toDto(trainingTypeRepository, userRepository)));
        traineeRepository.findById(traineeId).ifPresent(t -> traineeDto.set(t.toDto(userRepository)));
        trainingTypeRepository.findById(type).ifPresent(t -> trainingTypeDto.set(t.toDto()));

        return new TrainingDto(id, traineeDto.get(), trainerDto.get(), name, trainingTypeDto.get(), date, duration);
    }

    public static TrainingEntity fromDto(TrainingDto trainingDto) {
        return new TrainingEntity(
                trainingDto.getId(),
                EntityInterface.getIdFromDto(trainingDto.getTrainee()),
                EntityInterface.getIdFromDto(trainingDto.getTrainer()),
                trainingDto.getName(),
                EntityInterface.getIdFromDto(trainingDto.getType()),
                trainingDto.getDate(),
                trainingDto.getDuration()
        );
    }
}