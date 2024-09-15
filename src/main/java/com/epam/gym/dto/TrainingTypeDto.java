package com.epam.gym.dto;

import com.epam.gym.entity.TrainingTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;

@Data
@Builder
@ToString
@AllArgsConstructor
public class TrainingTypeDto implements Dto<BigInteger> {
    private BigInteger id;
    private TrainingTypeEnum name;
}