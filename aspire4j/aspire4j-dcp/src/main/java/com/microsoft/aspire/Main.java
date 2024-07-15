package com.microsoft.aspire;

import com.microsoft.aspire.resources.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class Main {
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        DcpOptions dcpOptions = new DcpOptions();
        dcpOptions.setBinPath("/Users/xiada/Documents/Projects/aspire4j/aspire4j/aspire4j-dcp/bin/darwin_arm64_0.5.7/ext/bin");
        dcpOptions.setCliPath("/Users/xiada/Documents/Projects/aspire4j/aspire4j/aspire4j-dcp/bin/darwin_arm64_0.5.7/dcp");
        dcpOptions.setExtensionsPath("/Users/xiada/Documents/Projects/aspire4j/aspire4j/aspire4j-dcp/bin/darwin_arm64_0.5.7/ext");

        DcpInfo dcpInfo = new DcpInfo();

        Locations locations = new Locations();
        
        DistributedApplication distributedApplication = new DistributedApplication().getInstance();
        Container container = new Container<>("test-redis");
        container.withImage("docker.io/library/redis:7.2");
        distributedApplication.addResource(container);

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