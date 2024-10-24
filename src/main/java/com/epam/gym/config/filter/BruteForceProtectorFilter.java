package com.epam.gym.config.filter;

import com.epam.gym.service.BruteForceProtectorService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class BruteForceProtectorFilter extends OncePerRequestFilter {
    private final BruteForceProtectorService bruteForceProtectorService;

    private final String LOGIN_ENDPOINT = "/login";

    private boolean isRequestLogin(HttpServletRequest request) {
        return request.getRequestURI().contains(LOGIN_ENDPOINT) && "POST".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("Starting BruteForceProtectorFilter");

        if (isRequestLogin(request)) {
            String clientIp = bruteForceProtectorService.getClientIP(request);

            log.trace("Client IP: {}", clientIp);

            if (clientIp != null && bruteForceProtectorService.isBlocked(clientIp)) {
                log.warn("User is blocked");

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("User is blocked. Try again later");
                return;
            }
        }

        log.debug(MarkerFactory.getMarker("SUCCESS"), "Passed BruteForceProtectorFilter");
        filterChain.doFilter(request, response);
    }
}