package com.microsoft.aspire.dcp.model.network;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Specifications for a Container Network.
 */
public class ContainerNetworkSpec {
    /**
     * Name of the network, auto-generated if omitted.
     */
    @JsonProperty("networkName")
    private String networkName; 

    /**
     * Enable IPv6 for the network?
     */
    @JsonProperty("ipv6")
    private Boolean ipv6;

    /**
     * Should this network persist between DCP runs?
     */
    @JsonProperty("persistent")
    private Boolean persistent;

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public Boolean getIpv6() {
        return ipv6;
    }

    public void setIpv6(Boolean ipv6) {
        this.ipv6 = ipv6;
    }

    public Boolean getPersistent() {
        return persistent;
    }

    public void setPersistent(Boolean persistent) {
        this.persistent = persistent;
    }
}
