package com.azure.example.petclinic;

import com.azure.runtime.host.DistributedApplication;
import com.azure.runtime.host.dcp.DcpAppHost;
import com.azure.runtime.host.extensions.spring.SpringExtension;

public class PetClinicAppHost implements DcpAppHost {
    
    public static void main(String[] args) {
        new PetClinicAppHost().boot(args);
    }

    @Override
    public void configureApplication(DistributedApplication app) {
        app.printExtensions();
        
        SpringExtension spring = app.withExtension(SpringExtension.class);
        
        spring.addSpringProject("spring-petclinic-config-server")
            .withExternalHttpEndpoints();
        
        spring.addSpringProject("spring-petclinic-discovery-server")
                .withExternalHttpEndpoints();
        
        spring.addSpringProject("spring-petclinic-customers-service")
                .withExternalHttpEndpoints();
        
        spring.addSpringProject("spring-petclinic-vets-service")
                .withExternalHttpEndpoints();
        
        spring.addSpringProject("spring-petclinic-visits-service")
                .withExternalHttpEndpoints();
        
        spring.addSpringProject("spring-petclinic-api-gateway")
                .withExternalHttpEndpoints();
    }
}
