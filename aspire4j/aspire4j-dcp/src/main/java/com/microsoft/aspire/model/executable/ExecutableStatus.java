package com.microsoft.aspire.model.executable;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.Getter;
import lombok.Setter;
import com.microsoft.aspire.model.common.EnvVar;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Represents the status of an executable, extending the Kubernetes V1Status.
 */
@Getter
@Setter
public class ExecutableStatus extends V1Status {
    /**
     * The execution ID is the identifier for the actual-state counterpart of the Executable.
     * For ExecutionType == Process it is the process ID, for ExecutionType == IDE it is the IDE session ID.
     */
    @JsonProperty("executionID")
    private String executionID;

    /**
     * Process ID of the executable when ExecutionType is Process.
     */
    @JsonProperty("pid")
    private int processId;

    /**
     * The current state of the process/IDE session started for this executable.
     */
    @JsonProperty("state")
    private String state = ExecutableState.UNKNOWN.getState();

    /**
     * Start (attempt) timestamp.
     */
    @JsonProperty("startupTimestamp")
    private OffsetDateTime startupTimestamp;

    /**
     * The time when the executable finished execution.
     */
    @JsonProperty("finishTimestamp")
    private OffsetDateTime finishTimestamp;

    /**
     * Exit code of the process associated with the Executable.
     */
    @JsonProperty("exitCode")
    private Integer exitCode;

    /**
     * The path of a temporary file that contains captured standard output data from the Executable process.
     */
    @JsonProperty("stdOutFile")
    private String stdOutFile;

    /**
     * The path of a temporary file that contains captured standard error data from the Executable process.
     */
    @JsonProperty("stdErrFile")
    private String stdErrFile;

    /**
     * Effective values of environment variables, after all substitutions have been applied.
     */
    @JsonProperty("effectiveEnv")
    private List<EnvVar> effectiveEnv;

    /**
     * Effective values of launch arguments to be passed to the Executable, after all substitutions are applied.
     */
    @JsonProperty("effectiveArgs")
    private List<String> effectiveArgs;
}
