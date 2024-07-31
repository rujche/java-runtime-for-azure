package com.azure.runtime.host.dcp.exceptions;

public class DistributedApplicationException extends RuntimeException {

    public DistributedApplicationException(String message) {
        super(message);
    }

    public DistributedApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
