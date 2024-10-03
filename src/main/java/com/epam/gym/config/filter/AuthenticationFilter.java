package com.epam.gym.config.filter;

import com.epam.gym.dto.model.request.UserCredentialModel;
import com.epam.gym.service.WebAuthService;
import com.epam.gym.util.UserProfileUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component(value = "authenticationFilter")
@AllArgsConstructor
public class AuthenticationFilter implements Filter {
    private final WebAuthService webAuthService;

    @Override
    // TODO: throw Unauthorized exception
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("Starting authentication filter");

        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;
        String authHeader = httpRequest.getHeader("Authorization");

        Optional<UserCredentialModel> credentials = UserProfileUtil.retrieveCredentialsFromBasicAuthHeader(authHeader);

        if (credentials.isPresent() && webAuthService.authenticate(credentials.get().username(), credentials.get().password())) {
            log.debug("Successfully passed the authentication filter");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Authentication failed");
        httpResponse.getWriter().close();
        log.debug("Failed to pass authentication filter");
    }
}