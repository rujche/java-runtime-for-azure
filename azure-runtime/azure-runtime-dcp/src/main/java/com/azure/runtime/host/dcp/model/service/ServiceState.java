package com.azure.runtime.host.dcp.model.service;

/**
 * Enumerates possible states of a Service.
 */
public enum ServiceState {
    /**
     * The service is not ready to accept connection. EffectiveAddress and EffectivePort do not contain final data.
     */
    NOT_READY("NotReady"),
    /**
     * The service is ready to accept connections.
     */
    READY("Ready");

    private final String state;

    ServiceState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
