package com.azure.runtime.host.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents a Java Component: Eureka Server for Spring.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JavaComponentEurekaServerForSpring extends Resource<JavaComponentEurekaServerForSpring> {

    public JavaComponentEurekaServerForSpring(String name) {
        super(ResourceType.JAVA_COMPONENT_EUREKA_SERVER_FOR_SPRING, name);
    }

    @Override
    public JavaComponentEurekaServerForSpring self() {
        return this;
    }
}
