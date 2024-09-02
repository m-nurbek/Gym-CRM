package com.epam.entity;

import com.epam.dto.TrainingTypeDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
@Data
public class TrainingTypeEntity implements Entity<BigInteger> {
    private BigInteger id;
    @NonNull
    private String name;

    @JsonCreator
    public TrainingTypeEntity(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("name") String name
    ) {
        this.id = id;
        this.name = name;
    }

    public TrainingTypeDto toDto() {
        return new TrainingTypeDto(id, name);
    }

    public static TrainingTypeEntity fromDto(TrainingTypeDto dto) {
        return new TrainingTypeEntity(dto.getId(), dto.getName());
    }
}