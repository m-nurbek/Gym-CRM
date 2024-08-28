package com.epam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TrainingEntity {
    private Long id;
    private Long traineeId;
    private Long trainerId;

    private String name;
    private Long type; // training type
    private Date date;
    private String duration;
}