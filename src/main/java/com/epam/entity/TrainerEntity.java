package com.epam.entity;

import com.epam.dto.TrainerDto;
import com.epam.dto.TrainingTypeDto;
import com.epam.dto.UserDto;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.Optional;

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
        Optional<TrainingTypeEntity> trainingTypeEntity = trainingTypeRepository.findById(specialization);
        Optional<UserEntity> userEntity = userRepository.findById(userId);

        TrainingTypeDto trainingTypeDto = null;
        UserDto userDto = null;

        if (trainingTypeEntity.isPresent()) {
            trainingTypeDto = trainingTypeEntity.get().toDto();
        }

        if (userEntity.isPresent()) {
            userDto = userEntity.get().toDto();
        }

        return new TrainerDto(id, trainingTypeDto, userDto);
    }

    public static TrainerEntity fromDto(TrainerDto trainerDto) {
        return new TrainerEntity(
                trainerDto.getId(),
                trainerDto.getSpecialization() == null ? BigInteger.ZERO : trainerDto.getSpecialization().getId(),
                trainerDto.getUser() == null ? BigInteger.ZERO : trainerDto.getUser().getId()
        );
    }
}