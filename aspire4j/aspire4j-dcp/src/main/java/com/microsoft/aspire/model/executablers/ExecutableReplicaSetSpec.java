package com.microsoft.aspire.model.executablers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Specification for the Executable Replica Set which details the desired state.
 */
@Getter
@Setter
public class ExecutableReplicaSetSpec {
    /**
     * Number of desired child Executable objects.
     */
    @JsonProperty("replicas")
    private int replicas = 1;

    /**
     * Template describing the configuration of child Executable objects created by the replica set.
     */
    @JsonProperty("template")
    private ExecutableTemplate template = new ExecutableTemplate();
}