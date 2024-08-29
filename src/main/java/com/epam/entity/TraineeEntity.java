package com.epam.entity;

import com.epam.dto.TraineeDto;
import com.epam.dto.UserDto;
import com.epam.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class TraineeEntity {
    private Long id;
    private Date dob;
    private String address;
    private Long userId;

    public TraineeDto toDto(UserRepository userRepository) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);

        UserDto userDto = null;

        if (userEntity.isPresent()) {
            userDto = userEntity.get().toDto();
        }

        return new TraineeDto(id, dob, address, userDto);
    }

    public static TraineeEntity fromDto(TraineeDto traineeDto) {
        return new TraineeEntity(traineeDto.getId(), traineeDto.getDob(), traineeDto.getAddress(), traineeDto.getUser().getId());
    }
}