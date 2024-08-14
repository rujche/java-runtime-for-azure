package com.azure.runtime.host.extensions.microservice.common;

import com.azure.runtime.host.DistributedApplication;
import com.azure.runtime.host.Extension;
import com.azure.runtime.host.extensions.microservice.common.resources.ConfigServerService;
import com.azure.runtime.host.extensions.microservice.common.resources.EurekaServiceDiscovery;
import com.azure.runtime.host.extensions.microservice.common.resources.ZipkinServerService;
import com.azure.runtime.host.resources.JavaComponentEurekaServerForSpring;

public abstract class MicroserviceExtension implements Extension {
    private final String name;
    private final String description;

    protected MicroserviceExtension(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Adds a new Eureka service to the app host.
     * @param name The name of the Eureka service.
     * @return A new {@link EurekaServiceDiscovery} instance that can be used to configure Eureka.
     */
    public EurekaServiceDiscovery addEurekaServiceDiscovery(String name) {
        return DistributedApplication.getInstance().addResource(new EurekaServiceDiscovery(name));
    }

    public JavaComponentEurekaServerForSpring addJavaComponentEurekaServerForSpring(String name) {
        return DistributedApplication.getInstance().addResource(new JavaComponentEurekaServerForSpring(name));
    }

    /**
     * Adds a new Config Server service to the app host.
     * @param name The name of the Config Server.
     * @return A new {@link ConfigServerService} instance that can be used to configure Config Server.
     */
    public ConfigServerService addConfigServer(String name) {
        return DistributedApplication.getInstance().addResource(new ConfigServerService(name));
    }

    /**
     * Adds a new Zipkin Server service to the app host.
     * @param name The name of the Zipkin Server.
     * @return A new {@link ZipkinServerService} instance that can be used to configure Zipkin Server.
     */
    public ZipkinServerService addZipkinServer(String name) {
        return DistributedApplication.getInstance().addResource(new ZipkinServerService(name));
    }
}
