package com.epam.gym.service;

import jakarta.servlet.http.HttpServletRequest;

public interface BruteForceProtectorService {

    void loginFailed(String clientIp);

    void loginSucceeded(String clientIp);

    boolean isBlocked(String clientIp);

    String getClientIP(HttpServletRequest httpServletRequest);
}