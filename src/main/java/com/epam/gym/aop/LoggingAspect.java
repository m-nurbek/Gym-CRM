package com.epam.gym.aop;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
@AllArgsConstructor
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

    @Pointcut("execution(* com.epam.gym.repository.*.*(..))")
    public void repositoryPointcut() {
        // Do nothing because of setting up a pointcut for Spring AOP.
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

    @Before(value = "controllerPointcut() || servicePointcut() || repositoryPointcut()")
    public void logBeforeWithTransactionId(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String transactionId = MDC.get("transactionId");

        log.trace(MarkerFactory.getMarker("LOGGING TRANSACTION"), ">> CALLED METHOD (Transaction ID: {}): {}() with args: {}", transactionId, methodName, args);
    }

    @AfterReturning(value = "controllerPointcut() || servicePointcut() || repositoryPointcut()", returning = "result")
    public void logAfterWithTransactionId(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String transactionId = MDC.get("transactionId");

        log.trace(MarkerFactory.getMarker("LOGGING TRANSACTION"), "<< METHOD RETURNED (Transaction ID: {}): {}() result: {}", transactionId, methodName, result);
    }

    @AfterThrowing(pointcut = "controllerPointcut() || servicePointcut() || repositoryPointcut()", throwing = "exception")
    public void logExceptionWithTransactionId(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String transactionId = MDC.get("transactionId");

        log.error(MarkerFactory.getMarker("LOGGING TRANSACTION"), "!! METHOD THREW EXCEPTION (Transaction ID: {}): {}() - {}", transactionId, methodName, exception.getMessage());
    }
}