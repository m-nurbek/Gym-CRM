package com.epam.gym.service;

public interface AuthService {
    boolean authenticate(String username, String password);

    boolean isAuthenticated(String username);

    boolean logout(String username);

    boolean removeUser(String username);

    void removeAllUsers();

    String[] register(String firstName, String lastName);
}