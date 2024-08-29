package com.epam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TraineeDto {
    private Long id;
    private Date dob;
    private String address;
    private UserDto user;
}