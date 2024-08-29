package com.epam.entity;

import com.epam.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class UserEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

    public UserDto toDto() {
        return new UserDto(id, firstName, lastName, username, password, isActive);
    }

    public static UserEntity fromDto(UserDto userDto) {
        return new UserEntity(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(), userDto.getPassword(), userDto.isActive());
    }
}