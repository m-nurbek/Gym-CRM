package com.epam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class TrainingTypeDto {
    private BigInteger id;
    private String name;
}