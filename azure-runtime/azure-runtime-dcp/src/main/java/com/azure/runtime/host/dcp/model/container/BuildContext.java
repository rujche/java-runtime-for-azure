package com.azure.runtime.host.dcp.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azure.runtime.host.dcp.model.common.EnvVar;

import java.util.List;

/**
 * Represents the build context details for Docker image construction.
 */
public class BuildContext {
    /**
     * The path to the directory that will serve as the root of the image build context.
     */
    @JsonProperty("context")
    private String context;

    /**
     * Optional path to a specific Dockerfile to use in the build (defaults to looking for a Dockerfile in the root Context folder).
     */
    @JsonProperty("dockerfile")
    private String dockerfile;

    /**
     * Optional build --build-args to pass to the build command.
     */
    @JsonProperty("args")
    private List<EnvVar> args;

    /**
     * Optional build secret mounts to pass to the build command.
     */
    @JsonProperty("secrets")
    private List<BuildContextSecret> secrets;

    /**
     * Optional specific stage to use when building a multiple stage Dockerfile.
     */
    @JsonProperty("stage")
    private String stage;

    /**
     * Optional additional tags to apply to the built image.
     */
    @JsonProperty("tags")
    private List<String> tags;

    /**
     * Optional labels to apply to the built image.
     */
    @JsonProperty("labels")
    private List<ContainerLabel> labels;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDockerfile() {
        return dockerfile;
    }

    public void setDockerfile(String dockerfile) {
        this.dockerfile = dockerfile;
    }

    public List<EnvVar> getArgs() {
        return args;
    }

    public void setArgs(List<EnvVar> args) {
        this.args = args;
    }

    public List<BuildContextSecret> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<BuildContextSecret> secrets) {
        this.secrets = secrets;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ContainerLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ContainerLabel> labels) {
        this.labels = labels;
    }
}
