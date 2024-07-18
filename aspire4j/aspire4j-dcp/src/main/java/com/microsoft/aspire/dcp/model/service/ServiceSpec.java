package com.microsoft.aspire.dcp.model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aspire.dcp.model.container.PortProtocol;

/**
 * Specifications for a Service, including network details.
 */
public class ServiceSpec {
    /**
     * The desired address for the service to run on.
     */
    @JsonProperty("address")
    private String address;

    /**
     * The desired port for the service to run on.
     */
    @JsonProperty("port")
    private Integer port;

    /**
     * The network protocol to be used for the service, defaults to TCP.
     */
    @JsonProperty("protocol")
    private String protocol = PortProtocol.TCP;

    /**
     * The mode for address allocation.
     */
    @JsonProperty("addressAllocationMode")
    private String addressAllocationMode = AddressAllocationModes.LOCALHOST.getMode();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddressAllocationMode() {
        return addressAllocationMode;
    }

    public void setAddressAllocationMode(String addressAllocationMode) {
        this.addressAllocationMode = addressAllocationMode;
    }
}
