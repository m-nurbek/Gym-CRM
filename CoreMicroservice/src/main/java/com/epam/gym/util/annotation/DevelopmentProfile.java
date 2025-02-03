package com.epam.gym.util.annotation;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Profile annotation to indicate that the bean will run only in the development environment
 * @author Nurbek on 03.02.2025
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Profile({"dev"})
public @interface DevelopmentProfile {
}