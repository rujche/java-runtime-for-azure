package com.microsoft.aspire;

import com.fasterxml.jackson.annotation.JsonProperty;

// DcpContainersInfo Class
public final class DcpContainersInfo {
    @JsonProperty("runtime")
    private String runtime;

    @JsonProperty("installed")
    private boolean installed;

    @JsonProperty("running")
    private boolean running;

    @JsonProperty("error")
    private String error;

    @JsonProperty("hostName")
    private String hostName;

    // Getters and Setters
    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getContainerHostName() {
        if (hostName != null) {
            return hostName;
        } else if ("podman".equals(runtime)) {
            return "host.containers.internal";
        } else {
            return "host.docker.internal";
        }
    }
}