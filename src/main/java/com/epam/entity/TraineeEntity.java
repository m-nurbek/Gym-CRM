package com.epam.entity;

import com.epam.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class TraineeEntity {
    private Long id;
    private Date date;
    private String address;
    private Long userId;
}