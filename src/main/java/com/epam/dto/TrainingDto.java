package com.epam.dto;

import java.util.Date;

public class TrainingDto {
    private Long id;
    private TraineeDto trainee;
    private TrainerDto trainer;

    private String name;
    private TrainingTypeDto type;
    private Date date;
    private String duration;
}