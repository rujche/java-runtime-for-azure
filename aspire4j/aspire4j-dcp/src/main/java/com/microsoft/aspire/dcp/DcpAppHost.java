package com.microsoft.aspire.dcp;

import com.microsoft.aspire.AppHost;
import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.resources.Container;

import java.util.concurrent.ExecutionException;

public interface DcpAppHost extends AppHost {

    @Override
    default void run() {
        DcpOptions dcpOptions = new DcpOptions();
        dcpOptions.setBinPath("/Users/xiada/Documents/Projects/aspire4j/aspire4j/aspire4j-dcp/bin/darwin_arm64_0.5.7/ext/bin");
        dcpOptions.setCliPath("/Users/xiada/Documents/Projects/aspire4j/aspire4j/aspire4j-dcp/bin/darwin_arm64_0.5.7/dcp");
        dcpOptions.setExtensionsPath("/Users/xiada/Documents/Projects/aspire4j/aspire4j/aspire4j-dcp/bin/darwin_arm64_0.5.7/ext");

        DcpInfo dcpInfo = new DcpInfo();

        Locations locations = new Locations();

        DistributedApplication distributedApplication = new DistributedApplication().getInstance();
//        Container container = new Container<>("test-redis");
//        container.withImage("docker.io/library/redis:7.2");
//        distributedApplication.addResource(container);

        DcpDependencyCheckServiceImpl dcpDependencyCheckService = new DcpDependencyCheckServiceImpl();

        ApplicationExecutor applicationExecutor = new ApplicationExecutor(
                distributedApplication,
                new KubernetesService(dcpOptions, locations),
                dcpOptions,
                dcpDependencyCheckService);

        DcpHostService dcpHostService = new DcpHostService(dcpOptions, applicationExecutor, dcpDependencyCheckService, locations);
        try {
            dcpHostService.startAsync().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
