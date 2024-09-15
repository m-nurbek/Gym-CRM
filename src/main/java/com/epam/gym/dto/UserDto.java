package com.epam.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;

@Data
@Builder
@ToString
@AllArgsConstructor
public class UserDto implements Dto<BigInteger> {
    private BigInteger id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;
}