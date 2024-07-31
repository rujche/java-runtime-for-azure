package com.azure.runtime.host.dcp.model.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class EndpointSpec {
    /**
     * Namespace of the service the endpoint implements
     */
    @JsonProperty("serviceNamespace")
    private String serviceNamespace;

    /**
     * Name of the service the endpoint implements
     */
    @JsonProperty("serviceName")
    private String serviceName;

    /**
     * The address of the endpoint
     */
    @JsonProperty("address")
    private String address;

    /**
     * The port of the endpoint
     */
    @JsonProperty("port")
    private Integer port;

    public String getServiceNamespace() {
        return serviceNamespace;
    }

    public void setServiceNamespace(String serviceNamespace) {
        this.serviceNamespace = serviceNamespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

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
}



