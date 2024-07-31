package com.azure.runtime.host.dcp.model.common;

public final class NamespacedName {
    private final String name;
    private final String namespace;

    public NamespacedName(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }
}




