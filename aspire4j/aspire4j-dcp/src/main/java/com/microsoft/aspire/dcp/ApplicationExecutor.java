package com.microsoft.aspire.dcp;

import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.dcp.k8s.IKubernetesService;
import com.microsoft.aspire.dcp.k8s.KubernetesService;
import com.microsoft.aspire.dcp.logging.AsyncIterable;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
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
//    private final Channel<LogInformationEntry> logInformationChannel = Channel.createUnbounded(new UnboundedChannelOptions().singleReader(true));

    private DcpInfo dcpInfo;
    private Future<?> resourceWatchTask;
    private DcpDependencyCheckService dcpDependencyCheckService;
    private IKubernetesService kubernetesService;

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

        return false;
    }

    private void watchResourceChanges() {

        var watchResourcesTask = CompletableFuture.runAsync(() -> {
//            try (outputSemaphore) {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(() -> watchKubernetesResourceAsync(Executable.class, this::processResourceChange)),
                    CompletableFuture.runAsync(() -> watchKubernetesResourceAsync(Container.class, this::processResourceChange))
//                        CompletableFuture.runAsync(() -> watchKubernetesResourceAsync(Service.class.getSimpleName(), this::processServiceChange)),
//                        CompletableFuture.runAsync(() -> watchKubernetesResourceAsync(Endpoint.class.getSimpleName(), this::processEndpointChange))
            ).join();
//            } catch (Exception e) {
//                logger.error("Error watching resources", e);
//            }
        });

        // FIXME add the logger service
//        var watchSubscribersTask = CompletableFuture.runAsync(() -> {
//            try {
//                for (var subscribers : loggerService.watchAnySubscribersAsync(cancellationToken)) {
//                    logInformationChannel.write(new LogInformation(subscribers.getName(), null, subscribers.hasAnySubscribers()));
//                }
//            } catch (Exception e) {
//                logger.error("Error watching subscribers", e);
//            }
//        });

        var watchSubscribersTask = CompletableFuture.runAsync(() -> {
            // Implementation here
        });
        var watchInformationChannelTask = updateLoggerState();

        resourceWatchTask = CompletableFuture.allOf(watchResourcesTask, watchSubscribersTask, watchInformationChannelTask);
    }

    private CompletableFuture<Void> updateLoggerState() {
        var watchInformationChannelTask = CompletableFuture.runAsync(() -> {
            var resourceLogState = new ConcurrentHashMap<String, Pair<Boolean, Boolean>>();

//            try {
//                for (var entry : logInformationChannel.readAllAsync(cancellationToken)) {
//                    var logsAvailable = resourceLogState.getOrDefault(entry.getResourceName(), new LogState(false, false)).isLogsAvailable();
//                    var hasSubscribers = resourceLogState.getOrDefault(entry.getResourceName(), new LogState(false, false)).isHasSubscribers();
//
//                    if (entry.getLogsAvailable() != null) {
//                        logsAvailable = entry.getLogsAvailable();
//                    }
//                    if (entry.getHasSubscribers() != null) {
//                        hasSubscribers = entry.getHasSubscribers();
//                    }
//
//                    if (logsAvailable) {
//                        if (hasSubscribers) {
//                            if (containersMap.containsKey(entry.getResourceName())) {
//                                startLogStream(containersMap.get(entry.getResourceName()));
//                            } else if (executablesMap.containsKey(entry.getResourceName())) {
//                                startLogStream(executablesMap.get(entry.getResourceName()));
//                            }
//                        } else {
//                            if (logStreams.containsKey(entry.getResourceName())) {
//                                logStreams.get(entry.getResourceName()).cancel();
//                                logStreams.remove(entry.getResourceName());
//                            }
//                            if (containersMap.containsKey(entry.getResourceName()) || executablesMap.containsKey(entry.getResourceName())) {
//                                loggerService.clearBacklog(entry.getResourceName());
//                            }
//                        }
//                    }
//
//                    resourceLogState.put(entry.getResourceName(), new LogState(logsAvailable, hasSubscribers));
//                }
//            } catch (Exception e) {
//                logger.error("Error reading log information channel", e);
//            }
        });
        return watchInformationChannelTask;
    }


    private <T extends CustomResource> void watchKubernetesResourceAsync(Class<T> resourceType, BiConsumer<String, T> handler) {
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
                var watcher = kubernetesService.watch(resourceType, null);

                while (watcher.hasNext()) {
                    var event = watcher.next();
//                outputSemaphore.acquire();
                    try {
                        handler.accept(event.type, event.object);
                    } finally {
//                    outputSemaphore.release();
                    }
                }
            }
//        );
    } catch (Exception e) {
        LOGGER.severe("Watch task over Kubernetes " + resourceType.getSimpleName() + " resources terminated unexpectedly. Check to ensure dcpd process is running." + e.getMessage());
    } finally {
        LOGGER.fine("Stopped watching " + resourceType.getSimpleName() +  " resources.");
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
                startLogStream(container);
                System.out.println("Container created: " + container.getMetadata().getName());
            }

            if (appResource.getDcpResource() instanceof Executable) {
                Executable executable = (Executable) appResource.getDcpResource();
                this.kubernetesService.create(Executable.class, executable);
                startLogStream(executable);
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
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Starting log streaming for " + resource.getMetadata().getName());
                    }

                    while (enumerable.iterator().hasNext()) {
                        List<LogEntry> batch = enumerable.iterator().next().get();
                        for (LogEntry entry : batch) {
                            var level = entry.isErrorMessage() ? Level.SEVERE : Level.INFO;
                            LOGGER.log(level, entry.getContent());
                        }
                    }

//                } catch (OperationCanceledException e) {
//                    logger.debug("Log streaming for {} was cancelled", resource.getMetadata().getName());
                } catch (Exception ex) {
                    LOGGER.severe("Error streaming logs for " + resource.getMetadata().getName());
                    LOGGER.severe(ex.getMessage());
                }
            });

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
    }
}
