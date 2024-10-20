package com.epam.gym.config.filter;

import com.epam.gym.service.BruteForceProtectorService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class BruteForceProtectorFilter extends OncePerRequestFilter {
    @Autowired
    private BruteForceProtectorService bruteForceProtectorService;

    private final String LOGIN_ENDPOINT_URL = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Starting BruteForceProtectorFilter");

        if (request.getRequestURI().endsWith(LOGIN_ENDPOINT_URL) && "POST".equalsIgnoreCase(request.getMethod())) {
            String clientIp = bruteForceProtectorService.getClientIP(request);

            log.debug("Client IP: {}", clientIp);

            if (clientIp != null && bruteForceProtectorService.isBlocked(clientIp)) {
                log.debug("User is blocked");

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("User is blocked. Try again later");
                return;
            }

            log.debug("User is not blocked");
        }

        log.debug("Passed BruteForceProtectorFilter");
        filterChain.doFilter(request, response);
    }
}