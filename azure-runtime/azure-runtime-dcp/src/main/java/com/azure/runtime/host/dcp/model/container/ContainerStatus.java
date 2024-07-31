package com.azure.runtime.host.dcp.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azure.runtime.host.dcp.model.common.Conventions;
import com.azure.runtime.host.dcp.model.common.EnvVar;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Extends the Kubernetes V1Status to include specific status information for containers.
 */
public class ContainerStatus /*extends V1Status*/ {
    /**
     * Container name displayed in Docker.
     */
    @JsonProperty("containerName")
    private String containerName;

    /**
     * Current state of the Container.
     */
    @JsonProperty("state")
    private String state;

    /**
     * ID of the Container (if an attempt to start the Container was made).
     */
    @JsonProperty("containerId")
    private String containerId;

    /**
     * Timestamp of the Container start attempt.
     */
    @JsonProperty("startupTimestamp")
    private OffsetDateTime startupTimestamp;

    /**
     * Timestamp when the Container was terminated last.
     */
    @JsonProperty("finishTimestamp")
    private OffsetDateTime finishTimestamp;

    /**
     * Exit code of the Container. Default is -1, meaning the exit code is not known, or the container is still running.
     */
    @JsonProperty("exitCode")
    private int exitCode = Conventions.UNKNOWN_EXIT_CODE; // Default value as per C# conventions.UnknownExitCode

    /**
     * Effective values of environment variables, after all substitutions have been applied.
     */
    @JsonProperty("effectiveEnv")
    private List<EnvVar> effectiveEnv;

    /**
     * Effective values of launch arguments to be passed to the Container, after all substitutions are applied.
     */
    @JsonProperty("effectiveArgs")
    private List<String> effectiveArgs;

    /**
     * Any ContainerNetworks this container is attached to.
     */
    @JsonProperty("networks")
    private List<String> networks;

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public OffsetDateTime getStartupTimestamp() {
        return startupTimestamp;
    }

    public void setStartupTimestamp(OffsetDateTime startupTimestamp) {
        this.startupTimestamp = startupTimestamp;
    }

    public OffsetDateTime getFinishTimestamp() {
        return finishTimestamp;
    }

    public void setFinishTimestamp(OffsetDateTime finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public List<EnvVar> getEffectiveEnv() {
        return effectiveEnv;
    }

    public void setEffectiveEnv(List<EnvVar> effectiveEnv) {
        this.effectiveEnv = effectiveEnv;
    }

    public List<String> getEffectiveArgs() {
        return effectiveArgs;
    }

    public void setEffectiveArgs(List<String> effectiveArgs) {
        this.effectiveArgs = effectiveArgs;
    }

    public List<String> getNetworks() {
        return networks;
    }

    public void setNetworks(List<String> networks) {
        this.networks = networks;
    }
}
