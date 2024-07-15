package com.microsoft.aspire.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.microsoft.aspire.model.common.CustomResource;

import java.util.HashMap;
import java.util.Map;

public class Schema {
    private final Map<Class<? extends CustomResource>, ResourceDetails> byType = new HashMap<>();
    
    public <T extends CustomResource> void add(Class<T> type, String kind, String resource) {
        byType.put(type, new ResourceDetails(kind, resource));
    }

    public <T extends CustomResource> ResourceDetails get(Class<T> type) {
        return byType.get(type);
    }

    @Getter
    @AllArgsConstructor
    public static class ResourceDetails {
        private String kind;
        private String resource;
    }
}
