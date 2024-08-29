package com.epam.entity;

import com.epam.dto.TrainingTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class TrainingTypeEntity {
    private Long id;
    private String name;

    public TrainingTypeDto toDto() {
        return new TrainingTypeDto(id, name);
    }

    public static TrainingTypeEntity fromDto(TrainingTypeDto dto) {
        return new TrainingTypeEntity(dto.getId(), dto.getName());
    }
}