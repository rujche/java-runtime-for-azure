package com.microsoft.aspire.model.executable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the configuration settings for launching a project.
 */
@Getter
@Setter
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
}
