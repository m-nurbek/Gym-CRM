package com.epam.gym.config.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component("loggingFilter")
public class LoggingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String transactionId = request.getHeader("transactionId");
        MDC.put("transactionId", transactionId);
        log.trace(MarkerFactory.getMarker("LOGGING TRANSACTION CREATED"), "Generated transaction ID for logging: {}", transactionId);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        log.trace(MarkerFactory.getMarker("LOGGING TRANSACTION DELETED"), "Cleared the transaction ID: {}", MDC.get("transactionId"));
        MDC.clear();
    }
}