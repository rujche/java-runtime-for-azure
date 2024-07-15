package com.microsoft.aspire.model.groupversion;

import com.microsoft.aspire.model.Schema;
import com.microsoft.aspire.model.container.Container;
import com.microsoft.aspire.model.endpoint.Endpoint;
import com.microsoft.aspire.model.executable.Executable;
import com.microsoft.aspire.model.executablers.ExecutableReplicaSet;
import com.microsoft.aspire.model.service.Service;

public class Dcp {
    public static final GroupVersion GROUP_VERSION = new GroupVersion("usvc-dev.developer.microsoft.com", "v1");
    public static final Schema SCHEMA = new Schema();
    
    public static final String EXECUTABLE_KIND = "Executable";
    public static final String CONTAINER_KIND = "Container";
    public static final String CONTAINER_NETWORK_KIND = "ContainerNetwork";
    public static final String SERVICE_KIND = "Service";
    public static final String ENDPOINT_KIND = "Endpoint";
    public static final String EXECUTABLE_REPLICA_SET_KIND = "ExecutableReplicaSet";

    static {
        SCHEMA.add(Executable.class, EXECUTABLE_KIND, "executables");
        SCHEMA.add(Container.class, CONTAINER_KIND, "containers");
        SCHEMA.add(Service.class, SERVICE_KIND, "services");
        SCHEMA.add(Endpoint.class, ENDPOINT_KIND, "endpoints");
        SCHEMA.add(ExecutableReplicaSet.class, EXECUTABLE_REPLICA_SET_KIND, "executablereplicasets");
    }
}