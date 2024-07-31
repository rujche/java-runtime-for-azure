package com.azure.runtime.host.dcp.model.endpoint;

import com.azure.runtime.host.dcp.model.common.CustomResource;
import com.azure.runtime.host.dcp.model.groupversion.Dcp;

public final class Endpoint extends CustomResource<EndpointSpec, EndpointStatus> {
    public Endpoint(EndpointSpec spec) {
        super(spec);
    }

    public static Endpoint create(String name, String serviceNamespace, String serviceName) {
        EndpointSpec spec = new EndpointSpec();
        spec.setServiceName(serviceName);
        spec.setServiceNamespace(serviceNamespace);

        Endpoint endpoint = new Endpoint(spec);
        endpoint.getMetadata().setName(name);
        endpoint.getMetadata().setNamespace("");

        return endpoint;
    }

    @Override
    public String getApiVersion() {
        return Dcp.GROUP_VERSION.toString();
    }

    @Override
    public String getKind() {
        return Dcp.ENDPOINT_KIND;
    }
}

