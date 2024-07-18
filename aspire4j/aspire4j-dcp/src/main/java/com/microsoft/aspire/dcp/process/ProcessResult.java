package com.microsoft.aspire.dcp.process;

/**
 * Represents the result of a process execution.
 */
public final class ProcessResult {
    private final int exitCode;

    /**
     * Constructs a ProcessResult with a given exit code.
     * @param exitCode The exit code of the process.
     */
    public ProcessResult(int exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * Returns the exit code of the process.
     * @return the exit code.
     */
    public int getExitCode() {
        return exitCode;
    }
}
