package com.epam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainerDto {
    private Long id;
    private TrainingTypeDto specialization;
    private UserDto user;
}