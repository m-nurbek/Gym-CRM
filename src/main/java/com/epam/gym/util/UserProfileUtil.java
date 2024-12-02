package com.epam.gym.util;

import lombok.experimental.UtilityClass;

import java.math.BigInteger;

@UtilityClass
public class UserProfileUtil {
    public static String generateUsername(String firstName, String lastName, BigInteger serialNumber) {
        return serialNumber.compareTo(BigInteger.ZERO) > 0 ? String.format("%s.%s%d", firstName, lastName, serialNumber) : String.format("%s.%s", firstName, lastName);
    }
}