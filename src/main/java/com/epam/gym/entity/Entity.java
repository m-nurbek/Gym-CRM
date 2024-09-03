package com.epam.gym.entity;

import com.epam.gym.dto.Dto;

import java.math.BigInteger;

public interface Entity<ID> {
    ID getId();

    void setId(ID id);

    static BigInteger getIdFromDto(Dto<BigInteger> dto) {
        return dto == null ? BigInteger.ZERO : dto.getId();
    }
}