package com.epam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TrainingDto {
    private Long id;
    private TraineeDto trainee;
    private TrainerDto trainer;

    private String name;
    private TrainingTypeDto type;
    private Date date;
    private String duration;
}