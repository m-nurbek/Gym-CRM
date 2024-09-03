package com.epam.gym.entity;

import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.repository.TrainingTypeRepository;
import com.epam.gym.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Data
public class TrainerEntity implements Entity<BigInteger> {
    private BigInteger id;
    @NonNull
    private BigInteger specialization; // training type
    @NonNull
    private BigInteger userId;

    @JsonCreator
    public TrainerEntity(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("specialization") BigInteger specialization,
            @JsonProperty("userId") BigInteger userId
    ) {
        this.id = id;
        this.specialization = specialization;
        this.userId = userId;
    }

    public TrainerDto toDto(TrainingTypeRepository trainingTypeRepository, UserRepository userRepository) {
        AtomicReference<TrainingTypeDto> trainingTypeDto = new AtomicReference<>();
        AtomicReference<UserDto> userDto = new AtomicReference<>();

        trainingTypeRepository.findById(specialization).ifPresent(s -> trainingTypeDto.set(s.toDto()));
        userRepository.findById(userId).ifPresent(u -> userDto.set(u.toDto()));

        return new TrainerDto(id, trainingTypeDto.get(), userDto.get());
    }

    public static TrainerEntity fromDto(TrainerDto trainerDto) {
        return new TrainerEntity(
                trainerDto.getId(),
                Entity.getIdFromDto(trainerDto.getSpecialization()),
                Entity.getIdFromDto(trainerDto.getUser())
        );
    }
}