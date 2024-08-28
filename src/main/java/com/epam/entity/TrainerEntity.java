package com.epam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class TrainerEntity {
    private Long id;
    private Long specialization; // training type
    private Long userId;
}