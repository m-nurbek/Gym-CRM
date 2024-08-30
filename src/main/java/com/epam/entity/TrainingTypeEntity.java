package com.epam.entity;

import com.epam.dto.TrainingTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class TrainingTypeEntity implements Entity<BigInteger> {
    private BigInteger id;
    @NonNull
    private String name;

    public TrainingTypeDto toDto() {
        return new TrainingTypeDto(id, name);
    }

    public static TrainingTypeEntity fromDto(TrainingTypeDto dto) {
        return new TrainingTypeEntity(dto.getId(), dto.getName());
    }
}