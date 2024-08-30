package com.epam.entity;

import com.epam.dto.UserDto;
import lombok.*;

import java.math.BigInteger;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class UserEntity implements Entity<BigInteger>{
    private BigInteger id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String username;
    @NonNull
    private String password;
    private boolean isActive = true;

    public UserDto toDto() {
        return new UserDto(id, firstName, lastName, username, password, isActive);
    }

    public static UserEntity fromDto(UserDto userDto) {
        return new UserEntity(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(), userDto.getPassword(), userDto.isActive());
    }
}