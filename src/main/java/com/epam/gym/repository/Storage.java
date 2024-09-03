package com.epam.gym.repository;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Scope("prototype")
public class Storage<T, ID> {
    private final Map<ID, T> map = new ConcurrentHashMap<>();
}