package com.microsoft.aspire.model.executablers;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Status for the Executable Replica Set, extending Kubernetes V1Status.
 */
@Getter
@Setter
public class ExecutableReplicaSetStatus extends V1Status {
    /**
     * Total number of observed child executables.
     */
    @JsonProperty("observedReplicas")
    private Integer observedReplicas;

    /**
     * Total number of current running child Executables.
     */
    @JsonProperty("runningReplicas")
    private Integer runningReplicas;

    /**
     * Total number of current Executable replicas that failed to start.
     */
    @JsonProperty("failedReplicas")
    private Integer failedReplicas;

    /**
     * Total number of current child Executables that have finished running.
     */
    @JsonProperty("finishedReplicas")
    private Integer finishedReplicas;

    /**
     * Last time the replica set was scaled up or down by the controller.
     */
    @JsonProperty("lastScaleTime")
    private OffsetDateTime lastScaleTime;
}