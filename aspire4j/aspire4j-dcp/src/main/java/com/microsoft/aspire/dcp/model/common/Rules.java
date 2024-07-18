package com.microsoft.aspire.dcp.model.common;

import java.util.regex.Pattern;

public final class Rules {
    public static boolean isValidObjectName(String candidate) {
        if (candidate == null || candidate.trim().isEmpty()) {
            return false;
        }

        if (candidate.length() > 253) {
            return false;
        }

        // Regex pattern in Java
        return Pattern.matches("^[a-zA-Z_~][a-zA-Z0-9\\-._~]*$", candidate);
    }
}