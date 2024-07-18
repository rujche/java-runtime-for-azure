package com.microsoft.aspire.dcp.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a network connection configuration for a container.
 */
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
}
