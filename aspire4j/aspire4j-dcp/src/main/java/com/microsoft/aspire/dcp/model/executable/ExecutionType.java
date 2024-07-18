package com.microsoft.aspire.dcp.model.executable;

/**
 * Defines the execution types for an executable.
 */
public enum ExecutionType {
    /**
     * Executable will be run directly by the controller, as a child process.
     */
    PROCESS("Process"),

    /**
     * Executable will be run via an IDE such as Visual Studio or Visual Studio Code.
     */
    IDE("IDE");

    private final String type;

    ExecutionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
