package com.microsoft.aspire.model.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kubernetes.client.openapi.models.V1Status;

import java.util.List;

/**
 * Represents the status of a Container Network, extending Kubernetes V1Status.
 */
public class ContainerNetworkStatus extends V1Status {
    /**
     * Current state of the network.
     */
    @JsonProperty("state")
    private String state; 

    /**
     * Network identifier.
     */
    @JsonProperty("id")
    private String id;

    /**
     * Network name.
     */
    @JsonProperty("networkName")
    private String networkName;

    /**
     * Network driver.
     */
    @JsonProperty("driver")
    private String driver;

    /**
     * Does the network support IPv6?
     */
    @JsonProperty("ipv6")
    private Boolean ipv6;

    /**
     * List of subnets allocated.
     */
    @JsonProperty("subnets")
    private List<String> subnets;

    /**
     * List of gateways allocated.
     */
    @JsonProperty("gateways")
    private List<String> gateways;

    /**
     * List of container IDs connected to the network.
     */
    @JsonProperty("containerIds")
    private List<String> containerIds;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Boolean getIpv6() {
        return ipv6;
    }

    public void setIpv6(Boolean ipv6) {
        this.ipv6 = ipv6;
    }

    public List<String> getSubnets() {
        return subnets;
    }

    public void setSubnets(List<String> subnets) {
        this.subnets = subnets;
    }

    public List<String> getGateways() {
        return gateways;
    }

    public void setGateways(List<String> gateways) {
        this.gateways = gateways;
    }

    public List<String> getContainerIds() {
        return containerIds;
    }

    public void setContainerIds(List<String> containerIds) {
        this.containerIds = containerIds;
    }
}
