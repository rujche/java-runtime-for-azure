package com.microsoft.aspire.dcp;

import com.microsoft.aspire.dcp.process.Pair;
import com.microsoft.aspire.dcp.process.ProcessResult;
import com.microsoft.aspire.dcp.process.ProcessSpec;
import com.microsoft.aspire.dcp.process.ProcessUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class DcpHostService implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(DcpHostService.class.getName());
    private static final int LOGGING_SOCKET_CONNECTION_BACKLOG = 3;
    private final ApplicationExecutor appExecutor;
//    private final Logger logger;
    private final DcpOptions dcpOptions;
//    private final DistributedApplicationExecutionContext executionContext;
    private final DcpDependencyCheckService dependencyCheckService;
    private final Locations locations;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final List<String> doNotInheritEnvironmentVars = List.of(
            "ASPNETCORE_URLS",
            "DOTNET_LAUNCH_PROFILE",
            "ASPNETCORE_ENVIRONMENT",
            "DOTNET_ENVIRONMENT"
    );
    private volatile boolean running = false;
    private CompletableFuture<Void> logProcessorTask;
    private AutoCloseable dcpRunDisposable;

    public DcpHostService(
            DcpOptions dcpOptions,
//            DistributedApplicationExecutionContext executionContext,
            ApplicationExecutor appExecutor,
            DcpDependencyCheckService dependencyCheckService,
            Locations locations) {
//        this.logger = logger;
        this.dcpOptions = dcpOptions;
//        this.executionContext = executionContext;
        this.appExecutor = appExecutor;
        this.dependencyCheckService = dependencyCheckService;
        this.locations = locations;
    }

    private boolean isSupported() {
//        return !executionContext.isPublishMode();
        return true;
    }

    public CompletableFuture<Void> startAsync() {
        if (!isSupported()) {
            return CompletableFuture.completedFuture(null);
        }

        DcpInfo dcpInfo = dependencyCheckService.getDcpInfo();
        ensureDcpHostRunning();
        appExecutor.runApplicationAsync();
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> stopAsync() {
        if (dcpOptions.isDeleteResourcesOnShutdown()) {
            appExecutor.deleteResourcesAsync()
                    .exceptionally(ex -> {
                        LOGGER.severe("Error deleting application resources: " + ex.getMessage());
                        return null;
                    });
        }

        running = false;
        return appExecutor.stopAsync()
                .thenCompose(v -> {
                    if (logProcessorTask != null) {
                        return logProcessorTask;
                    }
                    return CompletableFuture.completedFuture(null);
                });
    }

    @Override
    public void close() {
        if (dcpRunDisposable != null) {
            try {
                dcpRunDisposable.close();
            } catch (Exception ex) {
                LOGGER.severe("One or more monitoring tasks terminated with an error: " + ex.getMessage());
            }
        }
    }

    private void ensureDcpHostRunning() {
        // FIXME: Uncomment when AspireEventSource is available
//        AspireEventSource.getInstance().dcpApiServerLaunchStart();

        ProcessSpec dcpProcessSpec = createDcpProcessSpec(locations);

//            try {
        // FIXME: Uncomment when AspireEventSource is available
//                AspireEventSource.getInstance().dcpLogSocketCreateStart();
//                Socket loggingSocket = createLoggingSocket(locations.getDcpLogSocket());
//                loggingSocket.listen(LOGGING_SOCKET_CONNECTION_BACKLOG);

        dcpProcessSpec.getEnvironmentVariables().put("DCP_LOG_SOCKET", locations.getDcpLogSocket());

//                logProcessorTask = CompletableFuture.runAsync(() -> startLoggingSocket(loggingSocket));
//            } catch (IOException ex) {
//                logger.severe("Failed to enable orchestration logging: " + ex.getMessage());
//            } finally {
        // FIXME: Uncomment when AspireEventSource is available
//                AspireEventSource.getInstance().dcpLogSocketCreateStop();
//            }

        try {
            Pair<CompletableFuture<ProcessResult>, AutoCloseable> result = ProcessUtil.run(dcpProcessSpec);
            result.first().get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//            dcpRunDisposable = result.getDisposable();
//        } finally {
//            AspireEventSource.getInstance().dcpApiServerLaunchStop();
//        }
    }

    private ProcessSpec createDcpProcessSpec(Locations locations) {
        String dcpExePath = dcpOptions.getCliPath();
        if (!Files.exists(Paths.get(dcpExePath))) {
            throw new RuntimeException("The Aspire application host is not installed at \"" + dcpExePath + "\". The application cannot be run without it.");
        }

        String arguments = "start-apiserver --monitor " + ProcessHandle.current().pid() + " --detach --kubeconfig " + locations.getDcpKubeconfigPath() ;
        if (dcpOptions.getContainerRuntime() != null && !dcpOptions.getContainerRuntime().isEmpty()) {
            arguments += " --container-runtime " + dcpOptions.getContainerRuntime();
        }

        ProcessSpec dcpProcessSpec = new ProcessSpec(dcpExePath);
        dcpProcessSpec.setWorkingDirectory(System.getProperty("user.dir"));
        dcpProcessSpec.setArguments(arguments);
        dcpProcessSpec.setOnOutputData(System.out::println);
        dcpProcessSpec.setOnErrorData(System.err::println);
        dcpProcessSpec.setInheritEnv(false);

        LOGGER.info("Starting DCP with arguments: " + dcpProcessSpec.getArguments());

        Map<String, String> environment = System.getenv();
        for (Map.Entry<String, String> entry : environment.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!doNotInheritEnvironmentVars.contains(key)) {
                dcpProcessSpec.getEnvironmentVariables().put(key, value);
            }
        }

        if (dcpOptions.getExtensionsPath() != null && !dcpOptions.getExtensionsPath().isEmpty()) {
            dcpProcessSpec.getEnvironmentVariables().put("DCP_EXTENSIONS_PATH", dcpOptions.getExtensionsPath());
        }

        if (dcpOptions.getBinPath() != null && !dcpOptions.getBinPath().isEmpty()) {
            dcpProcessSpec.getEnvironmentVariables().put("DCP_BIN_PATH", dcpOptions.getBinPath());
        }

        dcpProcessSpec.getEnvironmentVariables().put("DCP_SESSION_FOLDER", locations.getDcpSessionDir());
        return dcpProcessSpec;
    }

//    private Socket createLoggingSocket(String socketPath) throws IOException {
//        Path path = Paths.get(socketPath);
//        Files.createDirectories(path.getParent());
//
//        Socket socket = new Socket(AddressFamily.UNIX, SocketType.STREAM, ProtocolFamily.UNSPEC);
//        socket.bind(new InetSocketAddress(socketPath, 0));
//        return socket;
//    }
//
//    private void startLoggingSocket(Socket socket) {
//        List<CompletableFuture<Void>> outputLoggers = new ArrayList<>();
//        try {
//            while (running) {
//                Socket acceptedSocket = socket.accept();
//                outputLoggers.add(CompletableFuture.runAsync(() -> logSocketOutput(acceptedSocket)));
//            }
//
//            CompletableFuture.allOf(outputLoggers.toArray(new CompletableFuture[0])).join();
//            socket.close();
//        } catch (IOException e) {
//            // Suppress exceptions reading logs from DCP controllers
//        }
//    }
//
//    private void logSocketOutput(Socket socket) {
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                LogInfo logInfo = getLogInfo(line);
//                Logger logger = logInfo.getLogger();
//                logger.log(logInfo.getLogLevel(), logInfo.getMessage());
//            }
//        } catch (IOException e) {
//            // Suppress exceptions reading logs from DCP controllers
//        }
//    }

//    private LogInfo getLogInfo(String line) {
//        String[] parts = line.split("\t", 4);
//        if (parts.length < 4) {
//            return new LogInfo(logger, Level.INFO, line);
//        }
//
//        Level logLevel = Level.INFO;
//        switch (parts[1]) {
//            case "info":
//                logLevel = Level.INFO;
//                break;
//            case "error":
//                logLevel = Level.SEVERE;
//                break;
//            case "warning":
//                logLevel = Level.WARNING;
//                break;
//            case "debug":
//                logLevel = Level.FINE;
//                break;
//            case "trace":
//                logLevel = Level.FINER;
//                break;
//        }
//
//        Logger categoryLogger = Logger.getLogger("Aspire.Hosting.Dcp." + parts[2]);
//        return new LogInfo(categoryLogger, logLevel, parts[3]);
//    }
//
//    private static class LogInfo {
//        private final Logger logger;
//        private final Level logLevel;
//        private final String message;
//
//        public LogInfo(Logger logger, Level logLevel, String message) {
//            this.logger = logger;
//            this.logLevel = logLevel;
//            this.message = message;
//        }
//
//        public Logger getLogger() {
//            return logger;
//        }
//
//        public Level getLogLevel() {
//            return logLevel;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//    }
}
