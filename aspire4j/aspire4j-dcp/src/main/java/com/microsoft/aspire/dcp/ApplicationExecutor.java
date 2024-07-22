package com.microsoft.aspire.dcp;

import com.google.common.reflect.TypeToken;
import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.dcp.k8s.IKubernetesService;
import com.microsoft.aspire.dcp.k8s.KubernetesService;
import com.microsoft.aspire.dcp.logging.AsyncChannel;
import com.microsoft.aspire.dcp.logging.AsyncIterable;
import com.microsoft.aspire.dcp.logging.AsyncIterator;
import com.microsoft.aspire.dcp.logging.CustomFormatter;
import com.microsoft.aspire.dcp.logging.LogEntry;
import com.microsoft.aspire.dcp.logging.ResourceLogSource;
import com.microsoft.aspire.dcp.metadata.DcpDependencyCheckService;
import com.microsoft.aspire.dcp.metadata.DcpInfo;
import com.microsoft.aspire.dcp.metadata.DcpOptions;
import com.microsoft.aspire.dcp.model.common.CustomResource;
import com.microsoft.aspire.dcp.model.common.EnvVar;
import com.microsoft.aspire.dcp.model.container.BuildContext;
import com.microsoft.aspire.dcp.model.container.Container;
import com.microsoft.aspire.dcp.model.endpoint.Endpoint;
import com.microsoft.aspire.dcp.model.executable.Executable;
import com.microsoft.aspire.dcp.model.service.Service;
import com.microsoft.aspire.dcp.process.Pair;
import com.microsoft.aspire.dcp.resource.AppResource;
import com.microsoft.aspire.dcp.utils.RandomNameGenerator;
import com.microsoft.aspire.dcp.utils.ResourceNameGenerator;
import com.microsoft.aspire.extensions.spring.resources.SpringProject;
import com.microsoft.aspire.resources.DockerFile;
import com.microsoft.aspire.resources.Resource;
import com.microsoft.aspire.resources.annotations.KeyValueAnnotation;
import io.kubernetes.client.util.Watch;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ApplicationExecutor {

    private static final Logger LOGGER = Logger.getLogger(ApplicationExecutor.class.getName());
    private static final String DEBUG_SESSION_PORT_VAR = "DEBUG_SESSION_PORT";
    private static final int RANDOM_NAME_SUFFIX_LENGTH = 8;

    //    private final Logger logger;
    private final DistributedApplication model;
    private final Map<String, Resource<?>> applicationModel;
    //    private final IDistributedApplicationLifecycleHook[] lifecycleHooks;
//    private final DistributedApplicationOptions distributedApplicationOptions;
    private final DcpOptions options;
    //    private final DistributedApplicationExecutionContext executionContext;
    private final List<AppResource> appResources = new ArrayList<>();
    //    private final CancellationTokenSource shutdownCancellation = new CancellationTokenSource();
    private final ConcurrentMap<String, Container> containersMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Executable> executablesMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Service> servicesMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Endpoint> endpointsMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<String>> resourceAssociatedServicesMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<Resource, Boolean> hiddenResources = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, CompletableFuture<Void>> logStreams = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<>();
    private final AsyncChannel<LogInformationEntry> logInformationChannel;

    private DcpInfo dcpInfo;
    private Future<?> resourceWatchTask;
    private DcpDependencyCheckService dcpDependencyCheckService;
    private IKubernetesService kubernetesService;
    private Map<String, Pair<Boolean, Boolean>> resourceLogState = new ConcurrentHashMap();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public ApplicationExecutor(
//            Logger logger,
            DistributedApplication model,
            KubernetesService kubernetesService,
//            List<IDistributedApplicationLifecycleHook> lifecycleHooks,
//            Environment environment,
//            DistributedApplicationOptions distributedApplicationOptions,
            DcpOptions options,
//            DistributedApplicationExecutionContext executionContext,
//            ResourceNotificationService notificationService,
//            ResourceLoggerService loggerService,
            DcpDependencyCheckService dcpDependencyCheckService) {
//        this.logger = logger;
        this.model = model;
        this.applicationModel = model.getResources();
//        this.lifecycleHooks = lifecycleHooks.toArray(new IDistributedApplicationLifecycleHook[0]);
//        this.distributedApplicationOptions = distributedApplicationOptions;
        this.options = options;
//        this.executionContext = executionContext;
        this.dcpDependencyCheckService = dcpDependencyCheckService;
        this.kubernetesService = kubernetesService;
        this.logInformationChannel = new AsyncChannel<>(Executors.newFixedThreadPool(20));
    }

    public void runApplicationAsync() {
//        AspireEventSource.getInstance().dcpModelCreationStart();

        try {
            this.dcpInfo = dcpDependencyCheckService.getDcpInfoAsync().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            prepareServices();
            prepareContainers();
            prepareExecutables();

            publishResourcesWithInitialState();

            watchResourceChanges();

            createServices();

            createContainersAndExecutables();

//            for (IDistributedApplicationLifecycleHook lifecycleHook : lifecycleHooks) {
//                lifecycleHook.afterResourcesCreated(model);
//            }
        } catch (Exception e) {
//            shutdownCancellation.cancel();
            throw e;
        } finally {
//            AspireEventSource.getInstance().dcpModelCreationStop();
        }
    }

    public CompletableFuture<Void> stopAsync() {
//        shutdownCancellation.cancel();
        List<Future<?>> tasks = new ArrayList<>();
        if (resourceWatchTask != null) {
            tasks.add(resourceWatchTask);
        }

//        tasks.addAll(logStreams.values().stream().map(entry -> {
//            entry.getKey().cancel();
//            return entry.getValue();
//        }).collect(Collectors.toList()));

        try {
            for (Future<?> task : tasks) {
                task.get();
            }
        } catch (Exception e) {
            LOGGER.fine("One or more monitoring tasks terminated with an error. " + e.getMessage());
        }
        return new CompletableFuture<>();
    }

    private void prepareServices() {
        // Implementation here

    }

    private void prepareContainers() {
        List<Resource<?>> modelContainerResources = this.model.getResources()
                .values()
                .stream()
                // only if it's a container or it's a dockerfile
                .filter(r -> r.getClass().equals(com.microsoft.aspire.resources.Container.class)
                        || r instanceof DockerFile)
                .collect(Collectors.toList());

        for (Resource container : modelContainerResources) {

            AppResource containerAppResource = null;

            if (container instanceof DockerFile) {

                if (findBaseImageBuildArg((DockerFile) container)) {
                    continue;
                }

                DockerFile dockerFile = (DockerFile) container;
                String nameSuffix = RandomNameGenerator.getRandomNameSuffix();
                String containerObjectName = ResourceNameGenerator.getObjectNameForResource(container, nameSuffix, "");
                Container ctr = Container.create(containerObjectName, null);
                BuildContext build = new BuildContext();

//                build.setArgs(buildArgs);
                build.setContext(dockerFile.getContext());
                build.setDockerfile(dockerFile.getPath());
                ctr.getSpec().setBuild(build);
                ctr.getSpec().setContainerName(containerObjectName); // Use the same name for container orchestrator (Docker, Podman) resource and DCP object name.

                ctr.annotate(CustomResource.RESOURCE_NAME_ANNOTATION, dockerFile.getName());
                ctr.annotate(CustomResource.OTEL_SERVICE_NAME_ANNOTATION, dockerFile.getName());
                ctr.annotate(CustomResource.OTEL_SERVICE_INSTANCE_ID_ANNOTATION, nameSuffix);

                containerAppResource = new AppResource(container, ctr);
            } else if (container instanceof com.microsoft.aspire.resources.Container) {
                String containerImageName = ((com.microsoft.aspire.resources.Container) container).getImage();
//            if (!container()) {
                // This should never happen! In order to get into this loop we need
                // to have the annotation, if we don't have the annotation by the time
                // we get here someone is doing something wrong.
//                throw new IllegalStateException();
//            } else {

//            }

                String nameSuffix = RandomNameGenerator.getRandomNameSuffix();
                String containerObjectName = ResourceNameGenerator.getObjectNameForResource(container, nameSuffix, "");
                Container ctr = Container.create(containerObjectName, containerImageName);

                ctr.getSpec().setContainerName(containerObjectName); // Use the same name for container orchestrator (Docker, Podman) resource and DCP object name.
                ctr.annotate(CustomResource.RESOURCE_NAME_ANNOTATION, container.getName());
                ctr.annotate(CustomResource.OTEL_SERVICE_NAME_ANNOTATION, container.getName());
                ctr.annotate(CustomResource.OTEL_SERVICE_INSTANCE_ID_ANNOTATION, nameSuffix);

                containerAppResource = new AppResource(container, ctr);
            }

            if (containerAppResource != null) {
                appResources.add(containerAppResource);
            }


            // FIXME: This is a temporary workaround to add the container to the map.
//            setInitialResourceState(container, ctr);
//
//            List<Mount> containerMounts;
//            if (container.tryGetContainerMounts()) {
//                containerMounts = container.getContainerMounts();
//                ctr.getSpec().setVolumeMounts(new ArrayList<>());
//
//                for (Mount mount : containerMounts) {
//                    VolumeMount volumeSpec = new VolumeMount();
//                    volumeSpec.setSource(mount.getSource());
//                    volumeSpec.setTarget(mount.getTarget());
//                    volumeSpec.setType(mount.getType() == ContainerMountType.BIND_MOUNT ? VolumeMountType.BIND : VolumeMountType.VOLUME);
//                    volumeSpec.setReadOnly(mount.isReadOnly());
//
//                    ctr.getSpec().getVolumeMounts().add(volumeSpec);
//                }
//            }


            // FIXME: This is a temporary workaround to add the container to the map.
//            addServicesProducedInfo(container, ctr, containerAppResource);

        }
    }

    private static boolean findBaseImageBuildArg(DockerFile dockerFile) {
        List<EnvVar> buildArgs = (List<EnvVar>) dockerFile.getAnnotations()
                .stream()
                .filter(a -> (a instanceof KeyValueAnnotation && ((KeyValueAnnotation) a).getType().equals("buildArgs")))
                .map(a -> createEnvVar((KeyValueAnnotation) a))
                .collect(Collectors.toList());

        return buildArgs.stream().anyMatch(arg -> arg.getName().equals("BASE_IMAGE"));
    }

    private static EnvVar createEnvVar(KeyValueAnnotation annotation) {
        return new EnvVar(annotation.getKey(), annotation.getValue().toString());
    }

    private void prepareExecutables() {
        List<Resource<?>> springProjects = this.model.getResources()
                .values()
                .stream()
                .filter(r -> r instanceof SpringProject).collect(Collectors.toList());
        for (Resource springProject : springProjects) {
            SpringProject project = (SpringProject) springProject;

            Executable executable = Executable.create(project.getName(), "mvn");
            executable.getSpec().setWorkingDirectory(project.getPath());
            executable.getSpec().setExecutablePath("mvn");
            executable.getSpec().setArgs(List.of("spring-boot:run"));

            this.appResources.add(new AppResource(project, executable));
        }
    }

    private void publishResourcesWithInitialState() {
        // Implementation here
    }

    private <T> boolean processResourceChange(String watchEventType, T resource) {

        System.out.println("Received resource change event: " + watchEventType + " for resource: " + resource);

        if ("ADDED".equals(watchEventType) || "MODIFIED".equals(watchEventType)) {

            if (resource instanceof Container) {
                System.out.println("Received container resource: " + ((Container) resource).getMetadata().getName());
                Container container = (Container) resource;
                this.containersMap.put(container.getMetadata().getName(), container);
                LogInformationEntry logInformationEntry = new LogInformationEntry(container.getMetadata().getName(), container.isLogsAvailable(), false);
                try {
                    this.logInformationChannel.produce(logInformationEntry).get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            } else if (resource instanceof Executable) {
                System.out.println("Received executable resource: " + ((Executable) resource).getMetadata().getName());
                Executable executable = (Executable) resource;
                this.executablesMap.put(executable.getMetadata().getName(), executable);
                LogInformationEntry logInformationEntry = new LogInformationEntry(executable.getMetadata().getName(), executable.isLogsAvailable(), false);
                try {
                    this.logInformationChannel.produce(logInformationEntry).get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

        } else if ("DELETED".equals(watchEventType)) {

        }
        return true;
    }

    private void watchResourceChanges() {

        Semaphore outputSemaphore = new Semaphore(1);

        var watchResourcesTask = CompletableFuture.runAsync(() -> {
//            try (outputSemaphore) {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(() -> watchKubernetesResourceAsync(outputSemaphore, Executable.class, new TypeToken<Watch.Response<Executable>>() {
                    }.getType(), this::processResourceChange)),
                    CompletableFuture.runAsync(() -> watchKubernetesResourceAsync(outputSemaphore, Container.class, new TypeToken<Watch.Response<Container>>() {
                    }.getType(), this::processResourceChange))
//                        CompletableFuture.runAsync(() -> watchKubernetesResourceAsync(Service.class.getSimpleName(), this::processServiceChange)),
//                        CompletableFuture.runAsync(() -> watchKubernetesResourceAsync(Endpoint.class.getSimpleName(), this::processEndpointChange))
            ).join();
//            } catch (Exception e) {
//                logger.error("Error watching resources", e);
//            }
        });

        // FIXME add the logger service
        var watchSubscribersTask = CompletableFuture.runAsync(() -> {
//            try {
//                for (var subscribers : loggerService.watchAnySubscribersAsync(cancellationToken)) {
//                    logInformationChannel.write(new LogInformation(subscribers.getName(), null, subscribers.hasAnySubscribers()));
//                }
//            } catch (Exception e) {
//                LOGGER.severe("Error watching subscribers" + e.getMessage());
//            }
        });

        var watchInformationChannelTask = updateLoggerState();

        resourceWatchTask = CompletableFuture.allOf(watchResourcesTask, watchSubscribersTask, watchInformationChannelTask);
    }

    private CompletableFuture<Void> updateLoggerState() {
        var watchInformationChannelTask = CompletableFuture.runAsync(() -> {
            
            AsyncIterator<LogInformationEntry> iterator = this.logInformationChannel.iterator();
            while (iterator.hasNext()) {
                
                iterator.next().thenAccept(entry -> {

                    var logsAvailable = resourceLogState.getOrDefault(entry.getResourceName(), new Pair<>(false, false)).first();
                    var hasSubscribers = resourceLogState.getOrDefault(entry.getResourceName(), new Pair<>(false, false)).second();

                    if (entry.getLogsAvailable() != null) {
                        logsAvailable = entry.getLogsAvailable();
                    }
                    if (entry.getHasSubscribers() != null) {
                        hasSubscribers = entry.getHasSubscribers();
                    }

                    resourceLogState.put(entry.getResourceName(), new Pair<>(logsAvailable, hasSubscribers));


                    System.out.println("Updating log state for resource: " + entry.getResourceName() + " logsAvailable: " + logsAvailable + " hasSubscribers: " + hasSubscribers);
                    if (Boolean.TRUE.equals(logsAvailable)) {
                        // FIXME this should be replaced by the dashboard
                        try {
                            Path logPath = Path.of("logs/" + entry.getResourceName() + ".log");
                            Files.createDirectories(logPath.getParent());
                            Files.createFile(logPath);
                            FileHandler fileHandler = new FileHandler(logPath.toString(), true);
                            fileHandler.setFormatter(new CustomFormatter());
                            Logger logger = Logger.getLogger(entry.getResourceName());
                            logger.addHandler(fileHandler);

                            loggers.put(logger.getName(), logger);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                        if (containersMap.containsKey(entry.getResourceName())) {
                            System.out.println("Calling startLogStream for container: " + entry.getResourceName());
                            startLogStream(containersMap.get(entry.getResourceName()));
                        } else if (executablesMap.containsKey(entry.getResourceName())) {
                            System.out.println("Calling startLogStream for executable: " + entry.getResourceName());
                            startLogStream(executablesMap.get(entry.getResourceName()));
                        } else {
                            LOGGER.severe("Resource not found for log streaming: " + entry.getResourceName());
                        }

                        if (Boolean.TRUE.equals(hasSubscribers)) {

                        } else {
//                                    if (logStreams.containsKey(entry.getResourceName())) {
//                                        logStreams.get(entry.getResourceName()).cancel(true);
//                                        logStreams.remove(entry.getResourceName());
//                                    }
//                                    if (containersMap.containsKey(entry.getResourceName()) || executablesMap.containsKey(entry.getResourceName())) {
//                                        loggerService.clearBacklog(entry.getResourceName());
//                                    }
                        }
                    }
                });
            }
        });
        return watchInformationChannelTask;
    }


    private <T extends CustomResource> void watchKubernetesResourceAsync(Semaphore outputSemaphore,
                                                                         Class<T> resourceType,
                                                                         Type watchType,
                                                                         BiConsumer<String, T> handler) {
        try {
            LOGGER.fine("Watching over DCP " + resourceType.getSimpleName() + " resources.");

//            var retryStrategy = RetryStrategy.newBuilder()
//                    .maxAttempts(Integer.MAX_VALUE)
//                    .exponentialBackoff(TimeUnit.MILLISECONDS.toMillis(100), TimeUnit.SECONDS.toMillis(30))
//                    .handleExceptions(EndOfStreamException.class)
//                    .jitter(true)
//                    .onRetry(attempt -> {
//                        logger.debug("Long poll watch operation was ended by server after {} ms (iteration {}).",
//                                attempt.getTotalDelay(), attempt.getAttemptNumber(), attempt.getException());
//                    })
//                    .build();

//            retryStrategy.execute(() -> {

            while (true) {
                Thread.sleep(Duration.ofSeconds(5).toMillis());

                var watch = kubernetesService.watch(resourceType, null, watchType);

                while (watch.hasNext()) {
                    var event = watch.next();
//                    outputSemaphore.acquire();
                    try {
                        handler.accept(event.type, event.object);
                    } finally {
//                        outputSemaphore.release();
                    }
                }

            }
//        );
        } catch (Exception e) {
            LOGGER.severe("Watch task over Kubernetes " + resourceType.getSimpleName() + " resources terminated unexpectedly. Check to ensure dcpd process is running." + e.getMessage());
        } finally {
            LOGGER.fine("Stopped watching " + resourceType.getSimpleName() + " resources.");
        }
    }


    private void createServices() {
        // Implementation herex
    }

    private void createContainersAndExecutables() {
        // Implementation here
        this.appResources.forEach(appResource -> {
            if (appResource.getDcpResource() instanceof Container) {
                Container container = (Container) appResource.getDcpResource();
                this.kubernetesService.create(Container.class, container);
                System.out.println("Container created: " + container.getMetadata().getName());
            }

            if (appResource.getDcpResource() instanceof Executable) {
                Executable executable = (Executable) appResource.getDcpResource();
                this.kubernetesService.create(Executable.class, executable);
                System.out.println("Executable created: " + executable.getMetadata().getName());
            }
        });

    }

    private <T extends CustomResource> void startLogStream(T resource) {
        AsyncIterable<List<LogEntry>> enumerable;
        if (resource instanceof Container && ((Container) resource).isLogsAvailable()) {
            enumerable = new ResourceLogSource<>(kubernetesService, dcpInfo.getVersion(), resource);
        } else if (resource instanceof Executable && ((Executable) resource).isLogsAvailable()) {
            enumerable = new ResourceLogSource<>(kubernetesService, dcpInfo.getVersion(), resource);
        } else {
            enumerable = null;
        }

        if (enumerable == null) {
            return;
        }

        logStreams.computeIfAbsent(resource.getMetadata().getName(), key -> {

            var task = CompletableFuture.runAsync(() -> {
                try {
                    System.out.println("Starting log streaming for " + resource.getMetadata().getName() + " on " + Thread.currentThread().getName());

                    AsyncIterator<List<LogEntry>> iterator = enumerable.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().thenAccept(batch ->
                                {
                                    for (LogEntry entry : batch) {
                                        var level = entry.isErrorMessage() ? Level.SEVERE : Level.INFO;
                                        loggers.get(resource.getMetadata().getName()).log(level, entry.getContent());
                                    }
                                }
                        );

                    }
//                } catch (OperationCanceledException e) {
//                    logger.debug("Log streaming for {} was cancelled", resource.getMetadata().getName());
                } catch (Exception ex) {
                    LOGGER.severe("Error streaming logs for " + resource.getMetadata().getName());
                    LOGGER.severe(ex.getMessage());
                }
            }, executor);

            return task;
        });
    }

    public CompletableFuture<Void> deleteResourcesAsync() {
        return new CompletableFuture<>();
    }

    private static class LogInformationEntry {
        private final String resourceName;
        private final Boolean logsAvailable;
        private final Boolean hasSubscribers;

        public LogInformationEntry(String resourceName, Boolean logsAvailable, Boolean hasSubscribers) {
            this.resourceName = resourceName;
            this.logsAvailable = logsAvailable;
            this.hasSubscribers = hasSubscribers;
        }

        public String getResourceName() {
            return resourceName;
        }

        public Boolean getLogsAvailable() {
            return logsAvailable;
        }

        public Boolean getHasSubscribers() {
            return hasSubscribers;
        }
    }
}
