package com.microsoft.aspire.dcp;

import com.microsoft.aspire.resources.Resource;
import com.microsoft.aspire.dcp.model.common.CustomResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an application resource with associated model and DCP resource.
 */
public class AppResource {
    private final Resource modelResource;
    private final CustomResource dcpResource;
    private List<ServiceAppResource> servicesProduced = new ArrayList<>();
    private List<ServiceAppResource> servicesConsumed = new ArrayList<>();

    /**
     * Constructs an AppResource with the specified model resource and DCP resource.
     *
     * @param modelResource the model resource
     * @param dcpResource   the DCP resource
     */
    public AppResource(Resource modelResource, CustomResource dcpResource) {
        this.modelResource = modelResource;
        this.dcpResource = dcpResource;
    }

    public Resource getModelResource() {
        return modelResource;
    }

    public CustomResource getDcpResource() {
        return dcpResource;
    }

    public List<ServiceAppResource> getServicesProduced() {
        return servicesProduced;
    }

    public void setServicesProduced(List<ServiceAppResource> servicesProduced) {
        this.servicesProduced = servicesProduced;
    }

    public List<ServiceAppResource> getServicesConsumed() {
        return servicesConsumed;
    }

    public void setServicesConsumed(List<ServiceAppResource> servicesConsumed) {
        this.servicesConsumed = servicesConsumed;
    }
}
