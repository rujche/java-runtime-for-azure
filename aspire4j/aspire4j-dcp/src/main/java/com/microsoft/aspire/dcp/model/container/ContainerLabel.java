package com.microsoft.aspire.dcp.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a label that can be applied to a container.
 */
public class ContainerLabel {
    /**
     * The key of the label.
     */
    @JsonProperty("key")
    private String key;

    /**
     * The value of the label.
     */
    @JsonProperty("value")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}