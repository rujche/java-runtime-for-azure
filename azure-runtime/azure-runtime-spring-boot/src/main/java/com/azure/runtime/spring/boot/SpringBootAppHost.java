package com.azure.runtime.spring.boot;

import com.azure.runtime.spring.boot.implementation.SpringBootMavenPluginRunner;

import java.util.ArrayList;
import java.util.List;

public interface SpringBootAppHost {

    List<String> projectIds = new ArrayList<>();

    void configureApplication();

    default SpringBootAppHost addSpringBootProject(String groupId, String artifactId) {
        return addSpringBootProject(String.format("%s:%s", groupId, artifactId));
    }

    default SpringBootAppHost addSpringBootProject(String projectId) {
        projectIds.add(projectId);
        return this;
    }

    default void run() {
        configureApplication();
        var threads = SpringBootMavenPluginRunner.start(projectIds);
    }
}
