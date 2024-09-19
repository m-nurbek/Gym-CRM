package com.epam.gym.entity;

public interface EntityInterface<ID> {
    ID getId();

    void setId(ID id);
}