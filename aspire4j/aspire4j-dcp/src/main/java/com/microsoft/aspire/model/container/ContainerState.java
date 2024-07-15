package com.microsoft.aspire.model.container;

/**
 * Defines the possible states of a container.
 */
public enum ContainerState {
    /**
     * Pending is the initial Container state. No attempt has been made to run the container yet.
     */
    PENDING("Pending"),

    /**
     * Building indicates an image is being built from a Dockerfile, but a container hasn't been created yet.
     */
    BUILDING("Building"),

    /**
     * Starting indicates a container is in the process of starting (pulling images, waiting to join to initial networks, etc.)
     */
    STARTING("Starting"),

    /**
     * A start attempt was made, but it failed.
     */
    FAILED_TO_START("FailedToStart"),

    /**
     * Container has been started and is executing.
     */
    RUNNING("Running"),

    /**
     * Container is paused.
     */
    PAUSED("Paused"),

    /**
     * Container finished execution.
     */
    EXITED("Exited"),

    /**
     * Container is in the process of stopping (waiting for container processes to exit, etc.).
     */
    STOPPING("Stopping"),

    /**
     * Unknown means for some reason container state is unavailable.
     */
    UNKNOWN("Unknown");

    private final String state;

    ContainerState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
