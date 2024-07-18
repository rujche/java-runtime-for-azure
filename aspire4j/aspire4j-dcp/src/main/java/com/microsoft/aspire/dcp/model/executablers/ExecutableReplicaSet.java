package com.microsoft.aspire.dcp.model.executablers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.aspire.dcp.model.common.CustomResource;
import com.microsoft.aspire.dcp.model.groupversion.Dcp;

/**
 * Represents a set of executable replicas as a Kubernetes custom resource.
 */
public class ExecutableReplicaSet extends CustomResource<ExecutableReplicaSetSpec, ExecutableReplicaSetStatus> {

    /**
     * Constructs an ExecutableReplicaSet with the specified spec.
     * @param spec Spec defining the executable replica set.
     */
    @JsonCreator
    public ExecutableReplicaSet(ExecutableReplicaSetSpec spec) {
        super(spec);
    }

    /**
     * Factory method to create a new ExecutableReplicaSet instance.
     * @param name Name of the replica set.
     * @param replicas Number of replicas.
     * @param executablePath Path to the executable binary.
     * @return A new instance of ExecutableReplicaSet.
     */
    public static ExecutableReplicaSet create(String name, int replicas, String executablePath) {
        if (replicas <= 0) {
            throw new IllegalArgumentException("Replicas must be greater than zero.");
        }

        ExecutableReplicaSet ers = new ExecutableReplicaSet(new ExecutableReplicaSetSpec());
        ers.getSpec().setReplicas(replicas);
        ers.getMetadata().setName(name);
        ers.getMetadata().setNamespace("");
        ers.getSpec().getTemplate().getSpec().setExecutablePath(executablePath);

        return ers;
    }

    @Override
    public String getApiVersion() {
        return Dcp.GROUP_VERSION.toString();
    }

    @Override
    public String getKind() {
        return Dcp.EXECUTABLE_REPLICA_SET_KIND;
    }
    
}
