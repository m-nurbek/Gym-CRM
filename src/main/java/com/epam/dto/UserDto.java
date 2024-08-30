package com.epam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class UserDto {
    private BigInteger id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}