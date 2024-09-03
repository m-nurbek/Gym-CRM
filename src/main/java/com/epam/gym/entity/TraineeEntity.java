package com.epam.gym.entity;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Data
public class TraineeEntity implements Entity<BigInteger> {
    private BigInteger id;
    @NonNull
    private Date dob;
    @NonNull
    private String address;
    @NonNull
    private BigInteger userId;

    @JsonCreator
    public TraineeEntity(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("dob") Date dob,
            @JsonProperty("address") String address,
            @JsonProperty("userId") BigInteger userId
    ) {
        this.id = id;
        this.dob = dob;
        this.address = address;
        this.userId = userId;
    }

    public TraineeDto toDto(UserRepository userRepository) {
        AtomicReference<UserDto> userDto = new AtomicReference<>(null);
        userRepository.findById(userId).ifPresent(u -> userDto.set(u.toDto()));

        return new TraineeDto(id, dob, address, userDto.get());
    }

    public static TraineeEntity fromDto(TraineeDto traineeDto) {
        return new TraineeEntity(
                traineeDto.getId(),
                traineeDto.getDob(),
                traineeDto.getAddress(),
                Entity.getIdFromDto(traineeDto.getUser())
        );
    }
}