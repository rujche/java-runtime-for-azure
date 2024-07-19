package com.microsoft.aspire.dcp.exceptions;

public class DistributedApplicationException extends RuntimeException {

    public DistributedApplicationException(String message) {
        super(message);
    }

    public DistributedApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
