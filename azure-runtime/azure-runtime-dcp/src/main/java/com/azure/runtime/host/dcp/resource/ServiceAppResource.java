package com.azure.runtime.host.dcp.resource;

import com.azure.runtime.host.dcp.model.service.Service;
import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.resources.annotations.EndpointAnnotation;

import java.util.List;

/**
 * Represents a service application resource, extending AppResource.
 */
public class ServiceAppResource extends AppResource {
    private final EndpointAnnotation endpointAnnotation;

    /**
     * Constructs a ServiceAppResource with the specified model resource, service, and endpoint annotation.
     *
     * @param modelResource the model resource
     * @param service       the service resource
     * @param endpointAnnotation the endpoint annotation
     */
    public ServiceAppResource(Resource modelResource, Service service, EndpointAnnotation endpointAnnotation) {
        super(modelResource, service);
        this.endpointAnnotation = endpointAnnotation;
    }

    public Service getService() {
        return (Service) getDcpResource();
    }

    public EndpointAnnotation getEndpointAnnotation() {
        return endpointAnnotation;
    }

    @Override
    public List<ServiceAppResource> getServicesProduced() {
        throw new UnsupportedOperationException("Service resources do not produce any services");
    }

    @Override
    public List<ServiceAppResource> getServicesConsumed() {
        throw new UnsupportedOperationException("Service resources do not consume any services");
    }
}
