package com.epam.gym.service;

public interface BruteForceProtectorService {

    void loginFailed(String username);

    void loginSucceeded(String username);

    boolean isBlocked(String username);
}