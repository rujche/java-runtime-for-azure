package com.microsoft.aspire.model.container;

/**
 * Defines the policies for container restart behavior.
 */
public enum ContainerRestartPolicy {
    /**
     * Do not automatically restart the container when it exits (default).
     */
    NONE("no"),

    /**
     * Restart only if the container exits with a non-zero status.
     */
    ON_FAILURE("on-failure"),

    /**
     * Restart the container, except if the container is explicitly stopped or the container daemon is stopped/restarted.
     */
    UNLESS_STOPPED("unless-stopped"),

    /**
     * Always try to restart the container.
     */
    ALWAYS("always");

    private final String policy;

    ContainerRestartPolicy(String policy) {
        this.policy = policy;
    }

    public String getPolicy() {
        return policy;
    }
}
