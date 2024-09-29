package com.epam.gym.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

    @Pointcut("@annotation(com.epam.gym.aop.Loggable)")
    public void executeLogging() {
        // Do nothing because of setting up a pointcut for Spring AOP.
    }

    @Pointcut("execution(* com.epam.gym.service.*.*(..))")
    public void servicePointcut() {
        // Do nothing because of setting up a pointcut for Spring AOP.
    }

    @Pointcut("execution(* com.epam.gym.controller.*.*(..))")
    public void controllerPointcut() {
        // Do nothing because of setting up a pointcut for Spring AOP.
    }

    @Before(value = "executeLogging() || servicePointcut() || controllerPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.debug(">> CALLED METHOD: {}() with args: {}", methodName, args);
    }

    @AfterReturning(value = "executeLogging() || servicePointcut() || controllerPointcut()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.debug("<< METHOD RETURNED: {}() result: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "executeLogging() || servicePointcut() || controllerPointcut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.debug("!! ERROR: {}() - {}", methodName, exception.getMessage());
    }
}