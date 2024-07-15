package com.microsoft.aspire.model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the status of a Service, extending Kubernetes V1Status.
 */
@Getter
@Setter
public class ServiceStatus extends V1Status {
    /**
     * he actual address the service is running on
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
}
