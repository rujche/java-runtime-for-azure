package com.microsoft.aspire.dcp;

import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.dcp.model.common.CustomResource;
import com.microsoft.aspire.dcp.model.container.Container;
import com.microsoft.aspire.dcp.model.endpoint.Endpoint;
import com.microsoft.aspire.dcp.model.executable.Executable;
import com.microsoft.aspire.dcp.model.service.Service;
import com.microsoft.aspire.extensions.spring.resources.SpringProject;
import com.microsoft.aspire.resources.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
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

        this.dcpInfo = dcpDependencyCheckService.getDcpInfo();

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
            LOGGER.fine("One or more monitoring tasks terminated with an error. " +  e.getMessage());
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
                .filter(r -> r instanceof com.microsoft.aspire.resources.Container).collect(Collectors.toList());

        for (Resource container : modelContainerResources) {
            String containerImageName;
            containerImageName = ((com.microsoft.aspire.resources.Container) container).getImage();
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

            AppResource containerAppResource = new AppResource(container, ctr);
            // FIXME: This is a temporary workaround to add the container to the map.
//            addServicesProducedInfo(container, ctr, containerAppResource);
            appResources.add(containerAppResource);
        }
    }

    private void prepareExecutables() {
        List<Resource<?>> springProjects = this.model.getResources()
                .values()
                .stream()
                .filter(r -> r.getType().equals("project.spring.v0")).collect(Collectors.toList());
        for (Resource springProject : springProjects) {
            SpringProject project = (SpringProject) springProject;


        }
    }

    private void publishResourcesWithInitialState() {
        // Implementation here
    }

    private void watchResourceChanges() {
        // Implementation here
    }

    private void createServices() {
        // Implementation here
    }

    private void createContainersAndExecutables() {
        // Implementation here
        this.appResources.forEach(appResource -> {
            Container container = (Container) appResource.getDcpResource();
            this.kubernetesService.create(Container.class, container);
            System.out.println("Container created: " + container.getMetadata().getName());
        });
        
    }

    public CompletableFuture<Void> deleteResourcesAsync()
    {
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
