package com.microsoft.aspire.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import com.microsoft.aspire.model.common.EnvVar;

import java.util.List;

/**
 * Represents the build context details for Docker image construction.
 */
@Getter
@Setter
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
}
