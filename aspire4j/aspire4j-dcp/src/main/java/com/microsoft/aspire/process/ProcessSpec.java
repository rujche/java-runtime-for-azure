package com.microsoft.aspire.process;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents the specification for a process execution.
 */
public final class ProcessSpec {
    private final String executablePath;
    
    private String workingDirectory;
    
    private Map<String, String> environmentVariables = new HashMap<>();
    
    private boolean inheritEnv = true;
    
    private String arguments;
    
    private Consumer<String> onOutputData;
    
    private Consumer<String> onErrorData;
    
    private Consumer<Integer> onStart;
    
    private Consumer<Integer> onStop;
    
    private boolean killEntireProcessTree = true;
    
    private boolean throwOnNonZeroReturnCode = true;

    /**
     * Constructs a ProcessSpec with the specified executable path.
     * @param executablePath The path to the executable.
     */
    public ProcessSpec(String executablePath) {
        this.executablePath = executablePath;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getExecutablePath() {
        return executablePath;
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public boolean isInheritEnv() {
        return inheritEnv;
    }

    public void setInheritEnv(boolean inheritEnv) {
        this.inheritEnv = inheritEnv;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public Consumer<String> getOnOutputData() {
        return onOutputData;
    }

    public void setOnOutputData(Consumer<String> onOutputData) {
        this.onOutputData = onOutputData;
    }

    public Consumer<String> getOnErrorData() {
        return onErrorData;
    }

    public void setOnErrorData(Consumer<String> onErrorData) {
        this.onErrorData = onErrorData;
    }

    public Consumer<Integer> getOnStart() {
        return onStart;
    }

    public void setOnStart(Consumer<Integer> onStart) {
        this.onStart = onStart;
    }

    public Consumer<Integer> getOnStop() {
        return onStop;
    }

    public void setOnStop(Consumer<Integer> onStop) {
        this.onStop = onStop;
    }

    public boolean isKillEntireProcessTree() {
        return killEntireProcessTree;
    }

    public void setKillEntireProcessTree(boolean killEntireProcessTree) {
        this.killEntireProcessTree = killEntireProcessTree;
    }

    public boolean isThrowOnNonZeroReturnCode() {
        return throwOnNonZeroReturnCode;
    }

    public void setThrowOnNonZeroReturnCode(boolean throwOnNonZeroReturnCode) {
        this.throwOnNonZeroReturnCode = throwOnNonZeroReturnCode;
    }
}
