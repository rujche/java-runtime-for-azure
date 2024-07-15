package com.microsoft.aspire.model.container;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a volume mount configuration for a container.
 */
@Getter
@Setter
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
}