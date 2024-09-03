package com.epam.gym.dto;

public interface Dto<ID> {
    ID getId();

    void setId(ID id);
}