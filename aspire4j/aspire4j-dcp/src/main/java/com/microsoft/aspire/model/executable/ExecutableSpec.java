package com.microsoft.aspire.model.executable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import com.microsoft.aspire.model.common.EnvVar;

import java.util.List;

/**
 * Specifies the configuration details for an executable.
 */
@Getter
@Setter
public class ExecutableSpec {
    /**
     * Path to the executable binary.
     */
    @JsonProperty("executablePath")
    private String executablePath;

    /**
     * The working directory for the executable.
     */
    @JsonProperty("workingDirectory")
    private String workingDirectory;

    /**
     * Launch arguments to be passed to the executable.
     */
    @JsonProperty("args")
    private List<String> args;

    /**
     * Environment variables to be set for the executable.
     */
    @JsonProperty("env")
    private List<EnvVar> env;

    /**
     * Environment files to use to populate the executable environment during startup.
     */
    @JsonProperty("envFiles")
    private List<String> envFiles;

    /**
     * The execution type for the executable.
     */
    @JsonProperty("executionType")
    private String executionType;
}
