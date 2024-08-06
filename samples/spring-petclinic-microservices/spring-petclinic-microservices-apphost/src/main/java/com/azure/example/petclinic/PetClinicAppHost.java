package com.azure.example.petclinic;

import com.azure.runtime.host.DistributedApplication;
import com.azure.runtime.host.dcp.DcpAppHost;
import com.azure.runtime.host.extensions.microservice.common.resources.EurekaServiceDiscovery;
import com.azure.runtime.host.extensions.spring.SpringExtension;
import com.azure.runtime.host.extensions.spring.resources.SpringProject;

public class PetClinicAppHost implements DcpAppHost {
    
    public static void main(String[] args) {
        new PetClinicAppHost().boot(args);
    }

    @Override
    public void configureApplication(DistributedApplication app) {
        app.printExtensions();
        
        SpringExtension spring = app.withExtension(SpringExtension.class);
      
        EurekaServiceDiscovery discoveryServer = spring
                .addEurekaServiceDiscovery("eureka");
        
        SpringProject configServer = spring.addSpringProject("spring-petclinic-config-server")
                .withHttpEndpoint(8888)
                .withExternalHttpEndpoints();

        spring.addSpringProject("spring-petclinic-customers-service")
                .withDependency(configServer)
                .withDependency(discoveryServer)
                .withEnvironment("SERVER_PORT", "8081")
                .withEnvironment("SPRING_APPLICATION_NAME", "customers-service")
                .withReference(discoveryServer);

        spring.addSpringProject("spring-petclinic-vets-service")
                .withDependency(configServer)
                .withDependency(discoveryServer)
                .withEnvironment("SERVER_PORT", "8082")
                .withEnvironment("SPRING_APPLICATION_NAME", "vets-service")
                .withReference(discoveryServer);

        spring.addSpringProject("spring-petclinic-visits-service")
                .withDependency(configServer)
                .withDependency(discoveryServer)
                .withEnvironment("SERVER_PORT", "8083")
                .withEnvironment("SPRING_APPLICATION_NAME", "visits-service")
                .withReference(discoveryServer);

        spring.addSpringProject("spring-petclinic-api-gateway")
                .withDependency(configServer)
                .withDependency(discoveryServer)
                .withEnvironment("SERVER_PORT", "8080")
                .withEnvironment("SPRING_APPLICATION_NAME", "api-gateway")
                .withReference(discoveryServer);
    }
}