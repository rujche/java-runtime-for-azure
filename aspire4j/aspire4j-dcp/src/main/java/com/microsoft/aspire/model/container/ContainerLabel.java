package com.microsoft.aspire.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a label that can be applied to a container.
 */
@Getter
@Setter
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
}