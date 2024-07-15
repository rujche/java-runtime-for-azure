package com.microsoft.aspire.model.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the status of a Container Network, extending Kubernetes V1Status.
 */
@Getter
@Setter
public class ContainerNetworkStatus extends V1Status {
    @JsonProperty("state")
    private String state;  // Current state of the network.

    @JsonProperty("id")
    private String id;  // Network identifier.

    @JsonProperty("networkName")
    private String networkName;  // Network name.

    @JsonProperty("driver")
    private String driver;  // Network driver.

    @JsonProperty("ipv6")
    private Boolean ipv6;  // Does the network support IPv6?

    @JsonProperty("subnets")
    private List<String> subnets;  // List of subnets allocated.

    @JsonProperty("gateways")
    private List<String> gateways;  // List of gateways allocated.

    @JsonProperty("containerIds")
    private List<String> containerIds;  // List of container IDs connected to the network.
}
