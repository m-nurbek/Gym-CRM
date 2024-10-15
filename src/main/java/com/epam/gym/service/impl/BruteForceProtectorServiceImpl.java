package com.epam.gym.service.impl;

import com.epam.gym.service.BruteForceProtectorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class BruteForceProtectorServiceImpl implements BruteForceProtectorService {
    private int MAX_ATTEMPTS = 3;
    private int lockTimeDurationInMinutes = 1;
    private final long LOCK_TIME = TimeUnit.MINUTES.toMillis(lockTimeDurationInMinutes);

    /**
     * key -> username,
     * value -> number of attempts
     */
    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();

    /**
     * key -> username,
     * value -> time when the user with given username was locked
     */
    private final Map<String, Long> lockTime = new ConcurrentHashMap<>();


    @Override
    public void loginFailed(String username) {
        int count = attempts.getOrDefault(username, 0) + 1;
        attempts.put(username, count);

        if (count >= MAX_ATTEMPTS) {
            lockTime.put(username, System.currentTimeMillis());
        }
    }

    @Override
    public void loginSucceeded(String username) {
        attempts.remove(username);
        lockTime.remove(username);
    }

    @Override
    public boolean isBlocked(String username) {
        if (!lockTime.containsKey(username)) {
            return false;
        }

        if (System.currentTimeMillis() - lockTime.get(username) > LOCK_TIME) {
            lockTime.remove(username);
            return false;
        }

        return true;
    }
}