package com.microsoft.aspire.model.network;

/**
 * Enumerates the possible states of a Container Network.
 */
public enum ContainerNetworkState {
    PENDING("Pending"),
    RUNNING("Running"),
    FAILED_TO_START("FailedToStart"),
    REMOVED("Removed"),
    NOT_FOUND("NotFound");

    private final String state;

    ContainerNetworkState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
