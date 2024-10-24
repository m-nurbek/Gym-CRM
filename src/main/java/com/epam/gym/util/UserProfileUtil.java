package com.epam.gym.util;

import com.epam.gym.dto.request.UserCredentialDto;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;

@UtilityClass
public class UserProfileUtil {
    private static final String CHARACTERS;
    private static final int PASSWORD_LENGTH = 10;

    /**
     * This class provides a cryptographically strong random number generator (RNG).
     * A cryptographically strong random number minimally complies with
     * the statistical random number generator tests specified in FIPS 140-2.
     * Additionally, SecureRandom must produce non-deterministic output.
     * Therefore, any seed material passed to a SecureRandom object must be unpredictable,
     * and all SecureRandom output sequences must be cryptographically strong.
     */
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

    public static String generateUsername(String firstName, String lastName, BigInteger serialNumber) {
        return serialNumber.compareTo(BigInteger.ZERO) > 0 ? String.format("%s.%s%d", firstName, lastName, serialNumber) : String.format("%s.%s", firstName, lastName);
    }

    public static Optional<UserCredentialDto> retrieveCredentialsFromBasicAuthHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            int fromIndex = "Basic ".length();
            String base64Credentials = authHeader.substring(fromIndex);

            byte[] credentialsDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credentialsDecoded); // credentials = username:password

            final String[] values = credentials.split(":", 2);

            String username = values[0];
            String password = values[1];

            return Optional.of(new UserCredentialDto(username, password));
        }

        return Optional.empty();
    }
}