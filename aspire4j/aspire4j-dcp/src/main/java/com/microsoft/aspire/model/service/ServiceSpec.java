package com.microsoft.aspire.model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import com.microsoft.aspire.model.container.PortProtocol;

/**
 * Specifications for a Service, including network details.
 */
@Getter
@Setter
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
}
