package com.microsoft.aspire.model.common;

import java.util.regex.Pattern;

public final class NamespacedName {
    private final String name;
    private final String namespace;

    public NamespacedName(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }
}




