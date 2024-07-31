package com.azure.runtime.host.dcp.model.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.azure.runtime.host.dcp.model.common.CustomResource;
import com.azure.runtime.host.dcp.model.groupversion.Dcp;

/**
 * Represents a Kubernetes Custom Resource for Container Networks.
 */
public class ContainerNetwork extends CustomResource<ContainerNetworkSpec, ContainerNetworkStatus> {

    @JsonCreator
    public ContainerNetwork(ContainerNetworkSpec spec) {
        super(spec);
    }

    /**
     * Factory method to create a new Container Network.
     * @param name The network name.
     * @param useIpV6 Whether to enable IPv6.
     * @return A new ContainerNetwork instance.
     */
    public static ContainerNetwork create(String name, boolean useIpV6) {
        ContainerNetworkSpec spec = new ContainerNetworkSpec();
        spec.setNetworkName(name);
        spec.setIpv6(useIpV6);
        
        ContainerNetwork network = new ContainerNetwork(spec);
        network.getMetadata().setName(name);
        network.getMetadata().setNamespace("");

        return network;
    }

    @Override
    public String getApiVersion() {
        return Dcp.GROUP_VERSION.toString();
    }

    @Override
    public String getKind() {
        return Dcp.CONTAINER_NETWORK_KIND;
    }
}
