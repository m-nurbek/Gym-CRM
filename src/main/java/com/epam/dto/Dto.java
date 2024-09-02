package com.epam.dto;

public interface Dto<ID> {
    ID getId();

    void setId(ID id);
}