package com.microsoft.aspire.model.executable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the configuration settings for launching a project.
 */
public class ProjectLaunchConfiguration {

    /**
     * Always returns the fixed type "project" for serialization.
     */
    @JsonProperty("type")
    public String getType() {
        return "project";
    }

    /**
     * The launch mode, which can be either "Debug" or "NoDebug" depending on whether the debugger is attached.
     */
    @JsonProperty("mode")
    private String mode = System.getenv("DEBUG") != null ? ProjectLaunchMode.DEBUG.getMode() : ProjectLaunchMode.NO_DEBUG.getMode();

    /**
     * The path to the project.
     */
    @JsonProperty("project_path")
    private String projectPath = "";

    /**
     * The name of the launch profile used.
     */
    @JsonProperty("launch_profile")
    private String launchProfile = "";

    /**
     * Indicates whether the launch profile is disabled.
     */
    @JsonProperty("disable_launch_profile")
    private boolean disableLaunchProfile = false;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getLaunchProfile() {
        return launchProfile;
    }

    public void setLaunchProfile(String launchProfile) {
        this.launchProfile = launchProfile;
    }

    public boolean isDisableLaunchProfile() {
        return disableLaunchProfile;
    }

    public void setDisableLaunchProfile(boolean disableLaunchProfile) {
        this.disableLaunchProfile = disableLaunchProfile;
    }
}
