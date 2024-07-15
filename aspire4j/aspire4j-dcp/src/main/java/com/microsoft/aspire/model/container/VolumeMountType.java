package com.microsoft.aspire.model.container;

import lombok.Getter;
import lombok.Setter;

/**
 * Defines the types of volume mounts.
 */
public class VolumeMountType {
    /**
     * A volume mount to a host directory.
     */
    public static final String BIND = "bind";

    /**
     * A volume mount to a volume managed by the container orchestrator.
     */
    public static final String VOLUME = "volume";
}
