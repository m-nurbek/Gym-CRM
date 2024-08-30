package com.epam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
public class TraineeDto {
    private BigInteger id;
    private Date dob;
    private String address;
    private UserDto user;
}