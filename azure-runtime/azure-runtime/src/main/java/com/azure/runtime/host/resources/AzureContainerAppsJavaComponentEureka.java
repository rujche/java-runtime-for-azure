package com.azure.runtime.host.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents an Azure Container Apps Java component: Eureka server for spring.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AzureContainerAppsJavaComponentEureka extends Resource<AzureContainerAppsJavaComponentEureka> {

    public static final ResourceType AZURE_CONTAINER_APPS_JAVA_COMPONENT_EUREKA = ResourceType.fromString("azure.container.apps.java.component.eureka.v0");

    public AzureContainerAppsJavaComponentEureka(String name) {
        super(AZURE_CONTAINER_APPS_JAVA_COMPONENT_EUREKA, name);
    }

    @Override
    public AzureContainerAppsJavaComponentEureka self() {
        return this;
    }
}
