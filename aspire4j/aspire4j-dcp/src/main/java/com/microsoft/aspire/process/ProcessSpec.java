package main.java.process;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents the specification for a process execution.
 */
public final class ProcessSpec {
    @Getter
    private final String executablePath;
    @Getter
    @Setter
    private String workingDirectory;
    @Getter
    @Setter
    private Map<String, String> environmentVariables = new HashMap<>();
    @Getter
    @Setter
    private boolean inheritEnv = true;
    @Getter
    @Setter
    private String arguments;
    @Getter
    @Setter
    private Consumer<String> onOutputData;
    @Getter
    @Setter
    private Consumer<String> onErrorData;
    @Getter
    @Setter
    private Consumer<Integer> onStart;
    @Getter
    @Setter
    private Consumer<Integer> onStop;
    @Getter
    @Setter
    private boolean killEntireProcessTree = true;
    @Getter
    @Setter
    private boolean throwOnNonZeroReturnCode = true;

    /**
     * Constructs a ProcessSpec with the specified executable path.
     * @param executablePath The path to the executable.
     */
    public ProcessSpec(String executablePath) {
        this.executablePath = executablePath;
    }

    
}
