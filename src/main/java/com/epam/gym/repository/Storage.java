package com.epam.gym.repository;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Storage class has prototype scope because each instance needs
 * its own isolated state (i.e., separate map) to store entities independently,
 * avoiding shared state across different instances.
 *
 * @param <T> entity to store
 * @param <ID> id of the entity
 */
@Component
@Getter
@Scope("prototype")
public class Storage<T, ID> {
    private final Map<ID, T> map = new ConcurrentHashMap<>();
}