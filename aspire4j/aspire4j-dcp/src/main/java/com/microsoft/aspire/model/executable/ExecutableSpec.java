package com.microsoft.aspire.model.executable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aspire.model.common.EnvVar;

import java.util.List;

/**
 * Specifies the configuration details for an executable.
 */
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

    public String getExecutablePath() {
        return executablePath;
    }

    public void setExecutablePath(String executablePath) {
        this.executablePath = executablePath;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
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

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }
}
