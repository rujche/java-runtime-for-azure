package com.microsoft.aspire.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a network connection configuration for a container.
 */
@Getter
@Setter
public class ContainerNetworkConnection {
    /**
     * DCP Resource name of a ContainerNetwork to connect to.
     * A container won't start running until it can be connected to all specified networks.
     */
    @JsonProperty("name")
    private String name;

    /**
     * Aliases of the container on the network. This enables container DNS resolution.
     */
    @JsonProperty("aliases")
    private List<String> aliases;
}
