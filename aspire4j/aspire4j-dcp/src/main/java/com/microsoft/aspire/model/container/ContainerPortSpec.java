package com.microsoft.aspire.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Specifies the port configuration for a container.
 */
@Getter
@Setter
public class ContainerPortSpec {
    /**
     * Optional: If specified, this must be a valid port number, 0 < x < 65536.
     */
    @JsonProperty("hostPort")
    private Integer hostPort;

    /**
     * Required: This must be a valid port number, 0 < x < 65536.
     */
    @JsonProperty("containerPort")
    private Integer containerPort;

    /**
     * The network protocol to be used, defaults to TCP.
     */
    @JsonProperty("protocol")
    private String protocol = PortProtocol.TCP;

    /**
     * Optional: What host IP to bind the external port to.
     */
    @JsonProperty("hostIP")
    private String hostIP;
}
