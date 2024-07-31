package com.azure.runtime.host.dcp.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomNameGenerator {
    private static final int RANDOM_NAME_SUFFIX_LENGTH = 8;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new SecureRandom();

    public static String getRandomNameSuffix() {
        StringBuilder suffix = new StringBuilder(RANDOM_NAME_SUFFIX_LENGTH);
        for (int i = 0; i < RANDOM_NAME_SUFFIX_LENGTH; i++) {
            suffix.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return suffix.toString();
    }
}
