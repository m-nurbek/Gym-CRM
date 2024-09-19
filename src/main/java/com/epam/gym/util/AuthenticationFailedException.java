package com.epam.gym.util;

/**
 * Exception that is thrown when authentication failed.
 */
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}