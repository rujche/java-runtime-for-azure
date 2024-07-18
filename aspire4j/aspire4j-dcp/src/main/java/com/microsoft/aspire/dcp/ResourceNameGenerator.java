package com.microsoft.aspire.dcp;

import com.microsoft.aspire.resources.Resource;

public class ResourceNameGenerator {

    public static String getObjectNameForResource(Resource resource, String suffix, String globalSuffix) {
        return maybeWithSuffix(resource.getName(), suffix, globalSuffix);
    }

    private static String maybeWithSuffix(String s, String localSuffix, String globalSuffix) {
        boolean isLocalSuffixEmpty = isNullOrWhiteSpace(localSuffix);
        boolean isGlobalSuffixEmpty = isNullOrWhiteSpace(globalSuffix);

        if (isLocalSuffixEmpty && isGlobalSuffixEmpty) {
            return s;
        } else if (!isLocalSuffixEmpty && isGlobalSuffixEmpty) {
            return s + "-" + localSuffix;
        } else if (isLocalSuffixEmpty && !isGlobalSuffixEmpty) {
            return s + "-" + globalSuffix;
        } else {
            return s + "-" + localSuffix + "-" + globalSuffix;
        }
    }

    private static boolean isNullOrWhiteSpace(String s) {
        return s == null || s.trim().isEmpty();
    }
}
