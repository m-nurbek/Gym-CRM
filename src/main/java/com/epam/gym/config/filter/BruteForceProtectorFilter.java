package com.epam.gym.config.filter;

import com.epam.gym.service.BruteForceProtectorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class BruteForceProtectorFilter extends OncePerRequestFilter {
    @Autowired
    private BruteForceProtectorService bruteForceProtectorService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String LOGIN_ENDPOINT_URL = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Starting BruteForceProtectorFilter");

        var wrappedRequest = new ContentCachingRequestWrapper(request);

        if (wrappedRequest.getRequestURI().endsWith(LOGIN_ENDPOINT_URL) && "POST".equalsIgnoreCase(wrappedRequest.getMethod())) {

            try {
                Map<String, String> requestBody = objectMapper.readValue(wrappedRequest.getInputStream(), Map.class);
                String username = requestBody.get("username").trim();

                log.debug("Got username from the request body: {}", username);

                if (username != null && bruteForceProtectorService.isBlocked(username)) {
                    log.debug("User is blocked");

                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("User is blocked. Try again later");
                    return;
                }

                log.debug("User is not blocked");

            } catch (IOException e) {
                log.error(e.getMessage(), e);

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid request body");
                return;
            }
        }

        log.debug("Passed BruteForceProtectorFilter");
        filterChain.doFilter(wrappedRequest, response);
    }
}