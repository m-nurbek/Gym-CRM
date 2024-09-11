package com.epam.gym.entity;

import com.epam.gym.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "USER_TABLE")
public class UserEntity implements EntityInterface<BigInteger> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private BigInteger id;
    @NonNull
    @Column(name = "FIRST_NAME")
    private String firstName;
    @NonNull
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "IS_ACTIVE")
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