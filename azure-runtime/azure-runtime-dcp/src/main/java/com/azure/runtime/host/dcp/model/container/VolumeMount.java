package com.azure.runtime.host.dcp.model.container;

/**
 * Represents a volume mount configuration for a container.
 */
public class VolumeMount {
    /**
     * Type of volume mount: either 'bind' or 'volume'.
     */
    private String type = VolumeMountType.BIND; // Default type set to "bind"

    /**
     * For bind mounts: the host directory to mount.
     * For volume mounts: name of the volume to mount.
     */
    private String source;

    /**
     * The path within the container that the mount will use.
     */
    private String target;

    /**
     * Indicates if the mounted file system is supposed to be read-only.
     */
    private boolean isReadOnly = false;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }
}