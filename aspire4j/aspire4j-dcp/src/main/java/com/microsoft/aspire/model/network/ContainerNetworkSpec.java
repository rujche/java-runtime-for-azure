package com.microsoft.aspire.model.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Specifications for a Container Network.
 */
@Getter
@Setter
public class ContainerNetworkSpec {
    @JsonProperty("networkName")
    private String networkName;  // Name of the network, auto-generated if omitted.

    @JsonProperty("ipv6")
    private Boolean ipv6;  // Enable IPv6 for the network?

    @JsonProperty("persistent")
    private Boolean persistent;  // Should this network persist between DCP runs?
}
