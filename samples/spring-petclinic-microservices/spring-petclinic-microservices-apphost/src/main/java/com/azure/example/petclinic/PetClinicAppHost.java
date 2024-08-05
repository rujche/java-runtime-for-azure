package com.azure.example.petclinic;

import com.azure.runtime.spring.boot.SpringBootAppHost;

public class PetClinicAppHost implements SpringBootAppHost {

    @Override
    public void configureApplication() {
        this.addSpringBootProject("org.springframework.samples.petclinic.config", "spring-petclinic-config-server")
                .addSpringBootProject("org.springframework.samples.petclinic.discovery", "spring-petclinic-discovery-server")
                .addSpringBootProject("org.springframework.samples.petclinic.client", "spring-petclinic-customers-service")
                .addSpringBootProject("org.springframework.samples.petclinic.vets", "spring-petclinic-vets-service")
                .addSpringBootProject("org.springframework.samples.petclinic.visits", "spring-petclinic-visits-service")
                .addSpringBootProject("org.springframework.samples.petclinic.api", "spring-petclinic-api-gateway");
    }

    public static void main(String[] args) {
        new PetClinicAppHost().run();
    }
}
