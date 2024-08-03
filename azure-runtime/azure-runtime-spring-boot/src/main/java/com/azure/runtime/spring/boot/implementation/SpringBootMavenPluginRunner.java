package com.azure.runtime.spring.boot.implementation;

import org.apache.maven.cli.MavenCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.apache.maven.cli.MavenCli.MULTIMODULE_PROJECT_DIRECTORY;

public class SpringBootMavenPluginRunner {
    private static final String SPRING_BOOT_RUN = "spring-boot:run";
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootMavenPluginRunner.class);
    private static boolean initialized = false;

    /**
     * Start the Spring Boot applications by spring-boot-maven-plugin
     *
     * @param projectIds Each projectId should be named in this format: "groupId:artifactId"
     */
    public static List<Thread> start(List<String> projectIds) {
        return projectIds.stream()
                .map(SpringBootMavenPluginRunner::start)
                .toList();
    }

    /**
     * Start the Spring Boot application by spring-boot-maven-plugin
     *
     * @param projectId The projectId should be named in this format: "groupId:artifactId"
     */
    public static Thread start(String projectId) {
        LOGGER.info("Starting spring boot application by `mvn spring-boot:run`, projectId = {}.", projectId);
        initialize();
        var projects = "--projects=" + projectId;
        var requestArgs = new String[]{projects, SPRING_BOOT_RUN};
        // TODO(rujche) investigate the usage of classWorld
        // TODO(rujche) Configure the output stream
        Thread thread = new Thread(() -> MavenCli.doMain(requestArgs, null), projectId);
        thread.start();
        try {
            // Wait spring boot application started.
            // For example: sometimes app2 need to start after app1 started.
            // TODO (rujche) Use better way to confirmed app started, for example: check log of "Started ..."
            Thread.sleep(20_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return thread;
    }

    private static synchronized void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        var directory = System.getProperty("user.dir");
        System.setProperty(MULTIMODULE_PROJECT_DIRECTORY, directory);
    }
}
