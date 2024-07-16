package com.microsoft.aspire.model.executablers;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Specification for the Executable Replica Set which details the desired state.
 */
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

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }

    public ExecutableTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ExecutableTemplate template) {
        this.template = template;
    }
}