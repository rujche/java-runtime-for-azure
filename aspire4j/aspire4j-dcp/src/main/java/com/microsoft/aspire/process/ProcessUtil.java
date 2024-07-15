package com.microsoft.aspire.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Utility class for managing process execution.
 */
public class ProcessUtil {
    private static final long PROCESS_EXIT_TIMEOUT = 5L;

    /**
     * Runs a process based on the provided specifications.
     *
     * @param processSpec The specifications for the process to be run.
     * @return A tuple containing the task for process result and an auto-closeable resource for process management.
     */
    public static Pair<CompletableFuture<ProcessResult>, AutoCloseable> run(main.java.process.ProcessSpec processSpec) throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add(processSpec.getExecutablePath());
        commands.addAll(Arrays.asList(processSpec.getArguments().split(" ")));

        
        ProcessBuilder processBuilder = new ProcessBuilder(commands)
                .directory(processSpec.getWorkingDirectory() != null ? new File(processSpec.getWorkingDirectory()) : null)
                .redirectErrorStream(true);

        if (!processSpec.isInheritEnv()) {
            processBuilder.environment().clear();
        }

        
        processBuilder.command(commands);
        
        processBuilder.environment().putAll(processSpec.getEnvironmentVariables());

        Process process = processBuilder.start();
        CompletableFuture<ProcessResult> processFuture = new CompletableFuture<>();
        Consumer<String> onOutputData = processSpec.getOnOutputData();
        Consumer<String> onErrorData = processSpec.getOnErrorData();

        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (onOutputData != null) {
                        onOutputData.accept(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        outputThread.start();

        Thread errorThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (onErrorData != null) {
                        onErrorData.accept(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        errorThread.start();

        new Thread(() -> {
            try {
                int exitCode = process.waitFor();
                System.out.println("\nExited with code : " + exitCode);
                processFuture.complete(new ProcessResult(exitCode));
            } catch (InterruptedException e) {
                processFuture.completeExceptionally(e);
            }
        }).start();

        AutoCloseable processDisposer = () -> {
            if (process.isAlive()) {
                process.destroy();
                process.waitFor(PROCESS_EXIT_TIMEOUT, TimeUnit.SECONDS);
                if (process.isAlive()) {
                    process.destroyForcibly();
                }
            }
        };

        return new Pair<>(processFuture, processDisposer);
    }

    /**
     * Represents a result of a process execution.
     */
    public static final class ProcessResult {
        private final int exitCode;

        public ProcessResult(int exitCode) {
            this.exitCode = exitCode;
        }

        public int getExitCode() {
            return exitCode;
        }
    }

    /**
     * A utility class to hold a pair of objects.
     *
     * @param <A> The first object type.
     * @param <B> The second object type.
     */
    public static class Pair<A, B> {
        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }
}
