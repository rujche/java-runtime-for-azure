package com.azure.runtime.host.dcp.model.executable;

/**
 * Enumerates the possible states of an executable.
 */
public enum ExecutableState {
    /**
     * Executable was successfully started and was running last time we checked.
     */
    RUNNING("Running"),

    /**
     * Terminated means the executable was killed by the controller (e.g., as a result of scale-down, or object deletion).
     */
    TERMINATED("Terminated"),

    /**
     * Failed to start means the executable could not be started (e.g., because of an invalid path to the program file).
     */
    FAILED_TO_START("FailedToStart"),

    /**
     * Finished means the executable ran to completion.
     */
    FINISHED("Finished"),

    /**
     * Unknown means we are not tracking the actual-state counterpart of the executable (process or IDE run session).
     * This can happen if a controller launches a process and then terminates.
     */
    UNKNOWN("Unknown");

    private final String state;

    ExecutableState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
