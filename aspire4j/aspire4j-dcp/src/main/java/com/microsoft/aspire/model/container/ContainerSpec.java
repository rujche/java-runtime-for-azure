package com.microsoft.aspire.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import com.microsoft.aspire.model.common.EnvVar;

import java.util.List;

/**
 * Specifies the configuration for a container, including its name, image, volumes, ports, and other settings.
 */
@Getter
@Setter
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
}
