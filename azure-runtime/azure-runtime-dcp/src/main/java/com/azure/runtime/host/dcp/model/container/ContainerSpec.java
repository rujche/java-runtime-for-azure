package com.azure.runtime.host.dcp.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azure.runtime.host.dcp.model.common.EnvVar;

import java.util.List;

/**
 * Specifies the configuration for a container, including its name, image, volumes, ports, and other settings.
 */
public class ContainerSpec {
    /**
     * Container name displayed in Docker. If not specified, the metadata name plus a random suffix is used.
     */
    @JsonProperty("containerName")
    private String containerName;

    /**
     * Image to be used to create the container.
     */
    @JsonProperty("image")
    private String image;

    /**
     * Optional configuration to build an image from a Dockerfile instead of using a pre-built image.
     */
    @JsonProperty("build")
    private BuildContext build;

    /**
     * Volumes that should be mounted into the container.
     */
    @JsonProperty("volumeMounts")
    private List<VolumeMount> volumeMounts;

    /**
     * Exposed ports.
     */
    @JsonProperty("ports")
    private List<ContainerPortSpec> ports;

    /**
     * Environment variables to be used for the container.
     */
    @JsonProperty("env")
    private List<EnvVar> env;

    /**
     * Environment files to use to populate the container environment during startup.
     */
    @JsonProperty("envFiles")
    private List<String> envFiles;

    /**
     * Container restart policy.
     */
    @JsonProperty("restartPolicy")
    private String restartPolicy = ContainerRestartPolicy.NONE.getPolicy();

    /**
     * Command to run in the container (entrypoint).
     */
    @JsonProperty("command")
    private String command;

    /**
     * Arguments to pass to the command that starts the container.
     */
    @JsonProperty("args")
    private List<String> args;

    /**
     * Optional labels to apply to the container instance.
     */
    @JsonProperty("labels")
    private List<ContainerLabel> labels;

    /**
     * Additional arguments to pass to the container run command.
     */
    @JsonProperty("runArgs")
    private List<String> runArgs;

    /**
     * Indicates whether this container should be created and persisted between DCP runs.
     */
    @JsonProperty("persistent")
    private Boolean persistent;

    /**
     * Networks that the container should be connected to.
     */
    @JsonProperty("networks")
    private List<ContainerNetworkConnection> networks;

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BuildContext getBuild() {
        return build;
    }

    public void setBuild(BuildContext build) {
        this.build = build;
    }

    public List<VolumeMount> getVolumeMounts() {
        return volumeMounts;
    }

    public void setVolumeMounts(List<VolumeMount> volumeMounts) {
        this.volumeMounts = volumeMounts;
    }

    public List<ContainerPortSpec> getPorts() {
        return ports;
    }

    public void setPorts(List<ContainerPortSpec> ports) {
        this.ports = ports;
    }

    public List<EnvVar> getEnv() {
        return env;
    }

    public void setEnv(List<EnvVar> env) {
        this.env = env;
    }

    public List<String> getEnvFiles() {
        return envFiles;
    }

    public void setEnvFiles(List<String> envFiles) {
        this.envFiles = envFiles;
    }

    public String getRestartPolicy() {
        return restartPolicy;
    }

    public void setRestartPolicy(String restartPolicy) {
        this.restartPolicy = restartPolicy;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public List<ContainerLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ContainerLabel> labels) {
        this.labels = labels;
    }

    public List<String> getRunArgs() {
        return runArgs;
    }

    public void setRunArgs(List<String> runArgs) {
        this.runArgs = runArgs;
    }

    public Boolean getPersistent() {
        return persistent;
    }

    public void setPersistent(Boolean persistent) {
        this.persistent = persistent;
    }

    public List<ContainerNetworkConnection> getNetworks() {
        return networks;
    }

    public void setNetworks(List<ContainerNetworkConnection> networks) {
        this.networks = networks;
    }
}
