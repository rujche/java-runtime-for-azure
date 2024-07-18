package com.microsoft.aspire.dcp.model.executable;

/**
 * Enumerates the launch modes for a project.
 */
public enum ProjectLaunchMode {
    /**
     * Launch the project in debug mode.
     */
    DEBUG("Debug"),

    /**
     * Launch the project without debugging.
     */
    NO_DEBUG("NoDebug");

    private final String mode;

    ProjectLaunchMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
