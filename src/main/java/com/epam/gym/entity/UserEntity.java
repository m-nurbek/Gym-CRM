package com.epam.gym.entity;

import com.epam.gym.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
@Data
public class UserEntity implements Entity<BigInteger> {
    private BigInteger id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private String username;
    private String password;
    private boolean isActive = true;

    @JsonCreator
    public UserEntity(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("isActive") boolean isActive
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public UserDto toDto() {
        return new UserDto(id, firstName, lastName, username, password, isActive);
    }

    public static UserEntity fromDto(UserDto userDto) {
        return new UserEntity(
                userDto.getId(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.isActive()
        );
    }
}