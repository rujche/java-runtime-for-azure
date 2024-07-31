package com.azure.runtime.host.dcp.model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kubernetes.client.openapi.models.V1Status;

/**
 * Represents the status of a Service, extending Kubernetes V1Status.
 */
public class ServiceStatus extends V1Status {
    /**
     * The actual address the service is running on
     */
    @JsonProperty("effectiveAddress")
    private String effectiveAddress;

    /**
     * The actual port the service is running on
     */
    @JsonProperty("effectivePort")
    private Integer effectivePort;

    /**
     * The current state of the service
     */
    @JsonProperty("state")
    private String state;

    public String getEffectiveAddress() {
        return effectiveAddress;
    }

    public void setEffectiveAddress(String effectiveAddress) {
        this.effectiveAddress = effectiveAddress;
    }

    public Integer getEffectivePort() {
        return effectivePort;
    }

    public void setEffectivePort(Integer effectivePort) {
        this.effectivePort = effectivePort;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
