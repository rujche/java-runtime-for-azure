package com.microsoft.aspire.dcp;

import java.time.Duration;

/**
 * Options for DCP (Distributed Control Plane) configuration.
 */
public final class DcpOptions {
    /**
     * The path to the DCP executable used for Aspire orchestration.
     * Example: "C:\\Program Files\\dotnet\\packs\\Aspire.Hosting.Orchestration.win-x64\\8.0.0-preview.1.23518.6\\tools\\dcp.exe"
     */
    private String cliPath;

    /**
     * Optional path to a folder containing the DCP extension assemblies (dcpctrl, etc.).
     * Example: "C:\\Program Files\\dotnet\\packs\\Aspire.Hosting.Orchestration.win-x64\\8.0.0-preview.1.23518.6\\tools\\ext\\"
     */
    private String extensionsPath;

    /**
     * Optional path to a folder containing the Aspire Dashboard binaries.
     * Example: "..\\..\\..\\artifacts\\bin\\Aspire.Dashboard\\Debug\\net8.0\\Aspire.Dashboard.dll"
     */
    private String dashboardPath;

    /**
     * Optional path to a folder containing additional DCP binaries.
     * Example: "C:\\Program Files\\dotnet\\packs\\Aspire.Hosting.Orchestration.win-x64\\8.0.0-preview.1.23518.6\\tools\\ext\\bin\\"
     */
    private String binPath;

    /**
     * Optional container runtime to override default runtime for DCP containers.
     * Example: "podman"
     */
    private String containerRuntime;

    /**
     * How long the dependency check will wait (in seconds) for a response before timing out.
     * Timeout is disabled if set to zero or a negative value.
     */
    private int dependencyCheckTimeout = 25;

    /**
     * The suffix to use for resource names when creating resources in DCP.
     */
    private String resourceNameSuffix;

    /**
     * Whether to delete resources created by this application when the application is shut down.
     */
    private boolean deleteResourcesOnShutdown;

    /**
     * Whether to randomize ports used by resources during orchestration.
     */
    private boolean randomizePorts;

    /**
     * Number of retries for reading Kubernetes config.
     */
    private int kubernetesConfigReadRetryCount = 300;

    /**
     * Interval between retries for reading Kubernetes config (in milliseconds).
     */
    private int kubernetesConfigReadRetryIntervalMilliseconds = 100;

    /**
     * Timeout for service startup watch.
     */
    private Duration serviceStartupWatchTimeout = Duration.ofSeconds(10);

    public String getCliPath() {
        return cliPath;
    }

    public void setCliPath(String cliPath) {
        this.cliPath = cliPath;
    }

    public String getExtensionsPath() {
        return extensionsPath;
    }

    public void setExtensionsPath(String extensionsPath) {
        this.extensionsPath = extensionsPath;
    }

    public String getDashboardPath() {
        return dashboardPath;
    }

    public void setDashboardPath(String dashboardPath) {
        this.dashboardPath = dashboardPath;
    }

    public String getBinPath() {
        return binPath;
    }

    public void setBinPath(String binPath) {
        this.binPath = binPath;
    }

    public String getContainerRuntime() {
        return containerRuntime;
    }

    public void setContainerRuntime(String containerRuntime) {
        this.containerRuntime = containerRuntime;
    }

    public int getDependencyCheckTimeout() {
        return dependencyCheckTimeout;
    }

    public void setDependencyCheckTimeout(int dependencyCheckTimeout) {
        this.dependencyCheckTimeout = dependencyCheckTimeout;
    }

    public String getResourceNameSuffix() {
        return resourceNameSuffix;
    }

    public void setResourceNameSuffix(String resourceNameSuffix) {
        this.resourceNameSuffix = resourceNameSuffix;
    }

    public boolean isDeleteResourcesOnShutdown() {
        return deleteResourcesOnShutdown;
    }

    public void setDeleteResourcesOnShutdown(boolean deleteResourcesOnShutdown) {
        this.deleteResourcesOnShutdown = deleteResourcesOnShutdown;
    }

    public boolean isRandomizePorts() {
        return randomizePorts;
    }

    public void setRandomizePorts(boolean randomizePorts) {
        this.randomizePorts = randomizePorts;
    }

    public int getKubernetesConfigReadRetryCount() {
        return kubernetesConfigReadRetryCount;
    }

    public void setKubernetesConfigReadRetryCount(int kubernetesConfigReadRetryCount) {
        this.kubernetesConfigReadRetryCount = kubernetesConfigReadRetryCount;
    }

    public int getKubernetesConfigReadRetryIntervalMilliseconds() {
        return kubernetesConfigReadRetryIntervalMilliseconds;
    }

    public void setKubernetesConfigReadRetryIntervalMilliseconds(int kubernetesConfigReadRetryIntervalMilliseconds) {
        this.kubernetesConfigReadRetryIntervalMilliseconds = kubernetesConfigReadRetryIntervalMilliseconds;
    }

    public Duration getServiceStartupWatchTimeout() {
        return serviceStartupWatchTimeout;
    }

    public void setServiceStartupWatchTimeout(Duration serviceStartupWatchTimeout) {
        this.serviceStartupWatchTimeout = serviceStartupWatchTimeout;
    }
}
