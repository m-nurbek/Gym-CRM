package com.epam.gym.aop;

import com.epam.gym.service.AuthService;
import com.epam.gym.util.AuthenticationFailedException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
@Aspect
public class AuthenticatedAspect {
    private final AuthService authService;

    @Pointcut("@annotation(com.epam.gym.aop.Authenticated)")
    public void executeAuthentication() {
        // Do nothing because of setting up a pointcut for Spring AOP.
    }

    @Around(value = "executeAuthentication()")
    public Object callAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug(">> AUTHENTICATED METHOD: {}", joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();
        String username = (String) args[0];

        Object returnValue = null;

        if (authService.isAuthenticated(username)) {
            log.debug("User is authenticated.");
            returnValue = joinPoint.proceed();
        } else {
            log.debug("User is not authenticated.");
            throw new AuthenticationFailedException("User is not authenticated.");
        }

        log.debug("<< AUTHENTICATED METHOD: {}", joinPoint.getSignature().getName());

        return returnValue;
    }

}