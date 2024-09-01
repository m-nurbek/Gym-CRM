package com.epam.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class Storage<T, ID> {
    private final Map<ID, T> map = new ConcurrentHashMap<>();
}