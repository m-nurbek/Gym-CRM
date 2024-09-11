package com.epam.gym.entity;

import com.epam.gym.dto.TrainingTypeDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "TRAINING_TYPE")
public class TrainingTypeEntity implements EntityInterface<BigInteger> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private BigInteger id;
    @NonNull
    @Column(name = "NAME")
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