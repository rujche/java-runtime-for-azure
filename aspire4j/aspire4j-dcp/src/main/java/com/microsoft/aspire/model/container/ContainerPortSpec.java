package com.microsoft.aspire.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Specifies the port configuration for a container.
 */
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

    public Integer getHostPort() {
        return hostPort;
    }

    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
    }

    public Integer getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(Integer containerPort) {
        this.containerPort = containerPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }
}
