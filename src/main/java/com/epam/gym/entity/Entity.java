package com.epam.gym.entity;

public interface Entity<ID> {
    ID getId();

    void setId(ID id);
}