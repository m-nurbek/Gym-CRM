package com.epam.gym.service.impl;

import com.epam.gym.service.BruteForceProtectorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BruteForceProtectorServiceImpl implements BruteForceProtectorService {
    @Value("${security.login.max-attempts}")
    private int MAX_ATTEMPTS = 3;
    @Value("${security.login.lock-time.minutes}")
    private int lockTimeDurationInMinutes = 1;
    private final long LOCK_TIME = TimeUnit.MINUTES.toMillis(lockTimeDurationInMinutes);

    /**
     * key -> client IP,
     * value -> number of attempts
     */
    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();

    /**
     * key -> client IP,
     * value -> time when the user with current IP was locked
     */
    private final Map<String, Long> lockTime = new ConcurrentHashMap<>();

    @Override
    public void loginFailed(String clientIp) {
        int count = attempts.getOrDefault(clientIp, 0) + 1;
        attempts.put(clientIp, count);

        log.trace("Login attempts count: {}", count);

        if (count >= MAX_ATTEMPTS) {
            blockIp(clientIp);
            log.warn("Reached the maximum number of login attempts");
        }
    }

    @Override
    public void loginSucceeded(String clientIp) {
        log.debug(MarkerFactory.getMarker("SUCCESS"), "Logged in successfully, removing the client IP from the block list");

        unblockIp(clientIp);
    }

    @Override
    public boolean isBlocked(String clientIp) {
        if (!lockTime.containsKey(clientIp)) {
            log.debug("Client IP {} is not blocked", clientIp);
            return false;
        }

        if (System.currentTimeMillis() - lockTime.get(clientIp) > LOCK_TIME) {
            unblockIp(clientIp);

            log.debug("Client IP {} is not blocked", clientIp);
            return false;
        }

        log.warn("Client IP {} is blocked", clientIp);
        return true;
    }

    private void blockIp(String clientIp) {
        lockTime.put(clientIp, System.currentTimeMillis());
    }

    private void unblockIp(String clientIp) {
        lockTime.remove(clientIp);
        attempts.remove(clientIp);
    }

    @Override
    public String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}