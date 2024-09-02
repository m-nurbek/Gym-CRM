package com.epam.util;

import java.security.SecureRandom;
import java.util.Random;

public class UserProfileUtil {
    private static final String CHARACTERS;
    private static final int PASSWORD_LENGTH = 10;
    private static final Random RANDOM = new SecureRandom();

    static {
        CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    }

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return password.toString();
    }

    public static String generateUsername(String firstName, String lastName, int serialNumber) {
        return serialNumber > 0 ? String.format("%s.%s%d", firstName, lastName, serialNumber) : String.format("%s.%s", firstName, lastName);
    }
}