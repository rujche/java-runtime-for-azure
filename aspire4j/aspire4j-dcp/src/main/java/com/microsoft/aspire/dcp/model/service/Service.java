package com.microsoft.aspire.dcp.model.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.aspire.dcp.model.common.CustomResource;
import com.microsoft.aspire.dcp.model.groupversion.Dcp;

/**
 * Represents a Kubernetes Custom Resource for a Service, managing lifecycle and configuration.
 */
public class Service extends CustomResource<ServiceSpec, ServiceStatus> {

    @JsonCreator
    public Service(ServiceSpec spec) {
        super(spec);
    }

    /**
     * Factory method to create a new Service instance with basic settings.
     * @param name The name of the service.
     * @return A new Service instance.
     */
    public static Service create(String name) {
        Service s = new Service(new ServiceSpec());
        s.getMetadata().setName(name);
        s.getMetadata().setNamespace("");

        return s;
    }

    /**
     * Checks if the service has a complete address assigned.
     * @return true if both port and address are properly assigned.
     */
    public boolean hasCompleteAddress() {
        return getSpec().getPort() != null && getSpec().getPort() > 0 && getSpec().getAddress() != null && !getSpec().getAddress().isEmpty();
    }

    @Override
    public String getApiVersion() {
        return Dcp.GROUP_VERSION.toString();
    }

    @Override
    public String getKind() {
        return Dcp.SERVICE_KIND;
    }
}
