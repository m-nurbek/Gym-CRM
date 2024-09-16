package com.epam.gym.service;

public interface AuthService {
    boolean authenticate(String username, String password);

    boolean isAuthenticated(String username);

    boolean logout(String username);

    void logoutOfAllAccounts();

    String[] register(String firstName, String lastName);

    String getUsernameOfAuthenticatedAccount();
}