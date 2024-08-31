package com.epam.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

    @Pointcut("@annotation(Loggable)")
    public void executeLogging() {
    }

    @Before(value = "executeLogging()")
    public void logBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.debug(">> CALLED METHOD: {}() with args: {}", methodName, args);
    }

    @AfterReturning(value = "executeLogging()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.debug("<< METHOD RETURNED: {}() result: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "executeLogging()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.debug("!! ERROR: {}() - {}", methodName, exception.getMessage());
    }
}