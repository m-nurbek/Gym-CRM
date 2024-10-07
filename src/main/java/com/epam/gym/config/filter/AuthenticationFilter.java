package com.epam.gym.config.filter;

import com.epam.gym.dto.request.UserCredentialDto;
import com.epam.gym.service.WebAuthService;
import com.epam.gym.util.UserProfileUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Order(1)
@AllArgsConstructor
@WebFilter(urlPatterns = {
        "/api/v1/trainees/*",
        "/api/v1/trainers/*",
        "/api/v1/trainings/*",
        "/api/v1/training-types/*",
        "/api/v1/auth/change-password/*"})
public class AuthenticationFilter implements Filter {
    private final WebAuthService webAuthService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("Starting authentication filter");

        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;
        String authHeader = httpRequest.getHeader("Authorization");

        Optional<UserCredentialDto> credentials = UserProfileUtil.retrieveCredentialsFromBasicAuthHeader(authHeader);

        if (credentials.isPresent() && webAuthService.authenticate(credentials.get().username(), credentials.get().password())) {
            filterChain.doFilter(servletRequest, servletResponse);

            log.debug("Successfully passed the authentication filter");
            return;
        }

        log.error("Failed to pass authentication filter");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Authentication failed");
        httpResponse.getWriter().close();
    }
}