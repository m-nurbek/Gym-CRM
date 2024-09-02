package com.epam.entity;

import com.epam.dto.TraineeDto;
import com.epam.dto.UserDto;
import com.epam.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;

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
        Optional<UserEntity> userEntity = userRepository.findById(userId);

        UserDto userDto = null;

        if (userEntity.isPresent()) {
            userDto = userEntity.get().toDto();
        }

        return new TraineeDto(id, dob, address, userDto);
    }

    public static TraineeEntity fromDto(TraineeDto traineeDto) {
        return new TraineeEntity(
                traineeDto.getId(),
                traineeDto.getDob(),
                traineeDto.getAddress(),
                traineeDto.getUser() == null ? BigInteger.ZERO : traineeDto.getUser().getId()
        );
    }
}