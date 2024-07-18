package com.microsoft.aspire.dcp.model.network;

/**
 * Enumerates the possible states of a Container Network.
 */
public enum ContainerNetworkState {
    /**
     * The network is being created, this is the initial state
     */
    PENDING("Pending"),
    /**
     * The network was successfully created
     */
    RUNNING("Running"),
    /**
     * An attempt was made to create the network, but it failed
     */
    FAILED_TO_START("FailedToStart"),
    /**
     * Network was running at some point, but has been removed
     */
    REMOVED("Removed"),
    /**
     * An existing network was not found
     */
    NOT_FOUND("NotFound");

    private final String state;

    ContainerNetworkState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
