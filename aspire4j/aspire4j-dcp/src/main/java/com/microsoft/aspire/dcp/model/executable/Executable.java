package com.microsoft.aspire.dcp.model.executable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.aspire.dcp.model.common.CustomResource;
import com.microsoft.aspire.dcp.model.groupversion.Dcp;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an executable as a custom resource in Kubernetes.
 */
public class Executable extends CustomResource<ExecutableSpec, ExecutableStatus> {

    @Deprecated
    public static final String CSHARP_PROJECT_PATH_ANNOTATION = "csharp-project-path";
    @Deprecated
    public static final String CSHARP_LAUNCH_PROFILE_ANNOTATION = "csharp-launch-profile";
    @Deprecated
    public static final String CSHARP_DISABLE_LAUNCH_PROFILE_ANNOTATION = "csharp-disable-launch-profile";

    public static final String LAUNCH_CONFIGURATIONS_ANNOTATION = "executable.usvc-dev.developer.microsoft.com/launch-configurations";

    /**
     * Constructor for Executable with the specified spec.
     * @param spec Spec defining the executable.
     */
    @JsonCreator
    public Executable(ExecutableSpec spec) {
        super(spec);
    }
    
    public Executable() {
        super();
    }

    /**
     * Factory method to create a new Executable instance.
     * @param name Name of the executable.
     * @param executablePath Path to the executable binary.
     * @return A new instance of Executable.
     */
    public static Executable create(String name, String executablePath) {
        ExecutableSpec spec = new ExecutableSpec();
        spec.setExecutablePath(executablePath);
        Executable executable = new Executable(spec);
        executable.getMetadata().setName(name);
        executable.getMetadata().setNamespace("");

        return executable;
    }

    /**
     * Checks if logs are available based on the current state of the executable.
     * @return True if logs are available, otherwise false.
     */
    public boolean areLogsAvailable() {
        ExecutableStatus status = getStatus();
        return status != null && (
            ExecutableState.RUNNING.equals(status.getState()) ||
            ExecutableState.FINISHED.equals(status.getState()) ||
            ExecutableState.TERMINATED.equals(status.getState())
        );
    }

    /**
     * Sets the project launch configuration for this executable.
     * @param launchConfiguration The launch configuration to set.
     */
    public void setProjectLaunchConfiguration(ProjectLaunchConfiguration launchConfiguration) {
        this.annotate(LAUNCH_CONFIGURATIONS_ANNOTATION, ""); // Clear existing annotation, if any.
        this.annotateAsObjectList(LAUNCH_CONFIGURATIONS_ANNOTATION, launchConfiguration);
    }

    /**
     * Attempts to retrieve the project launch configuration for this executable.
     * @return True if the launch configuration is successfully retrieved, false otherwise.
     */
    public boolean tryGetProjectLaunchConfiguration(ProjectLaunchConfiguration launchConfiguration) {
        List<ProjectLaunchConfiguration> launchConfigurations = new ArrayList<>();
        if (this.tryGetAnnotationAsObjectList(LAUNCH_CONFIGURATIONS_ANNOTATION, launchConfigurations)) {
            launchConfiguration = launchConfigurations.isEmpty() ? null : launchConfigurations.get(0);
        }

        return launchConfiguration != null;
    }

    @Override
    public String getApiVersion() {
        return Dcp.GROUP_VERSION.toString();
    }

    @Override
    public String getKind() {
        return Dcp.EXECUTABLE_KIND;
    }

    public boolean isLogsAvailable() {
        return getStatus() != null 
                && (ExecutableState.RUNNING.getState().equals(this.getStatus().getState())
                || ExecutableState.FINISHED.getState().equals(this.getStatus().getState())
                || ExecutableState.TERMINATED.getState().equals(this.getStatus().getState())) ;
    }
        
}
