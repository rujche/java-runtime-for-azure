package com.microsoft.aspire.extensions.spring.implementation;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.microsoft.aspire.extensions.spring.resources.SpringProject;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SpringIntrospector {
    private static final Logger LOGGER = Logger.getLogger(SpringIntrospector.class.getName());

    private final Set<SpringDeploymentStrategy> strategies = new TreeSet<>();

    public SpringIntrospector() { }

    public Set<SpringDeploymentStrategy> introspect(SpringProject project) {
        LOGGER.info("Beginning introspection of Spring project: " + project.getName());

        /*
         * This is where we can look at the project and do things like:
         * - Look for a Dockerfile
         * - Look for a pom.xml or build.gradle file for any known configuration or dependencies
         * - Look for configuration files in known locations
         * - Parse Java source code to look for Spring annotations
         */
        Map<String, String> introspectionProperties = new LinkedHashMap<>();

        lookForBuildFiles(project, introspectionProperties);
        introspectJavaFiles(project);

        // TODO Take the introspection properties and turn it into a set of useful properties to pass to the aspire-manifest,
        // and modify the properties of the SpringProject directly
//        Map<String, String> properties = new LinkedHashMap<>();

        return strategies;
    }

    private void lookForBuildFiles(SpringProject project, Map<String, String> properties) {
        LOGGER.fine("Looking for build files in project: " + project.getName());

        // Look for a pom.xml file
        if (lookForFile(project, properties, "pom.xml")) {
            LOGGER.fine("Found pom.xml file");

            // determine strategies that will yield a successful maven build and deployment.
            // In particular:
            //   * Is there any build plugin to generate a dockerfile?
            //   * Is there configuration to generate an executable jar file?
            introspectPomXml(project, properties.get("pom.xml"));
        }

        // Look for a build.gradle file
        if (lookForFile(project, properties, "build.gradle")) {
            LOGGER.fine("Found build.gradle file");
            // determine strategies that will yield a successful gradle build and deployment
            introspectBuildGradle(project, properties.get("build.gradle"));
        }

        // Dockerfile and compose.yaml files
//        boolean hasDocker = false;
        if (lookForFile(project, properties, "Dockerfile")) {
            // with this strategy, we can short circuit and just substitute our spring project to instead be a Docker project.
            strategies.add(
                new SpringDeploymentStrategy(SpringDeploymentStrategy.DeploymentType.DOCKER_FILE, 1000)
                    .withCommand(properties.get("Dockerfile")));
        }
//        hasDocker |= lookForFile(project, properties, "docker-compose.yaml");
//        hasDocker |= lookForFile(project, properties, "compose.yaml");
//        if (hasDocker) {
//            LOGGER.fine("Found docker files");
//            // determine strategies that will yield a successful docker build and deployment
//        }
    }

    private void introspectPomXml(SpringProject project, String pomXmlPath) {
        // look to see if the pom uses the spring-boot-maven-plugin plugin to generate far jars
        // Look for dockerfile support in the pom

        boolean dockerPluginFound = false;
        SpringDeploymentStrategy deploymentStrategy = new SpringDeploymentStrategy(SpringDeploymentStrategy.DeploymentType.MAVEN_POM, 2100);
        try {
            File inputFile = new File(pomXmlPath);
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader(inputFile));

            List<Plugin> plugins = model.getBuild().getPlugins();
            for (Plugin plugin : plugins) {
                String groupId = plugin.getGroupId();
                String artifactId = plugin.getArtifactId();
                if ("com.spotify".equals(groupId) && "dockerfile-maven-plugin".equals(artifactId)) {
                    LOGGER.fine("Found Spotify Dockerfile Maven Plugin!");
                    LOGGER.fine("Use the following command to build the Docker image:");
                    LOGGER.fine("mvn package dockerfile:build");
                    dockerPluginFound = true;
                    deploymentStrategy.withCommand("mvn package dockerfile:build");
                } else if ("io.fabric8".equals(groupId) && "docker-maven-plugin".equals(artifactId)) {
                    LOGGER.fine("Found Fabric8 Docker Maven Plugin!");
                    LOGGER.fine("Use the following command to build the Docker image:");
                    LOGGER.fine("mvn package docker:build");
                    dockerPluginFound = true;
                    deploymentStrategy.withCommand("mvn package docker:build");
                }
            }

            if (!dockerPluginFound) {
                LOGGER.fine("No Docker plugin found in the pom.xml file.");

                List<Dependency> dependencies = model.getDependencies();
                boolean webDependencyFound = false;
                for (Dependency dependency : dependencies) {
                    String groupId = dependency.getGroupId();
                    String artifactId = dependency.getArtifactId();
                    if ("org.springframework.boot".equals(groupId) && "spring-boot-starter-web".equals(artifactId)) {
                        LOGGER.fine("Found Spring Boot Starter Web dependency!");
                        webDependencyFound = true;
                    } else if ("org.springframework.boot".equals(groupId) && "spring-boot-starter-webflux".equals(artifactId)) {
                        LOGGER.fine("Found Spring Boot Starter WebFlux dependency!");
                        webDependencyFound = true;
                    }
                }

                String command = "mvn containerise";
                command += " --context " + Paths.get(pomXmlPath).getParent();
                if (webDependencyFound) {
                    command += " --port 8082:8082";
                    command += " --env SERVER_PORT=8082";
                }

                deploymentStrategy.withCommand(command);
                strategies.add(deploymentStrategy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void introspectBuildGradle(SpringProject project, String buildGradlePath) {
        // look to see if the pom uses the spring-boot-maven-plugin plugin to generate far jars
        // Look for dockerfile support in the pom

        boolean dockerPluginFound = false;
        SpringDeploymentStrategy deploymentStrategy = new SpringDeploymentStrategy(SpringDeploymentStrategy.DeploymentType.GRADLE_BUILD, 2200);

        String dependencyToCheck = "org.springframework.boot:spring-boot-starter-web";
        try {
            if (checkDependencyInGradleFile(buildGradlePath, dependencyToCheck)) {
                LOGGER.fine("Found Spring Boot Starter Web dependency!");
            }

            String command = "gradle containerise";
            command += " --project-dir " + Paths.get(buildGradlePath).getParent();
            command += " --build-file " + buildGradlePath;
            deploymentStrategy.withCommand(command);
            strategies.add(deploymentStrategy);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean checkDependencyInGradleFile(String filePath, String dependency) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        Pattern pattern = Pattern.compile(".*['\"]" + Pattern.quote(dependency) + "['\"].*");

        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                reader.close();
                return true;
            }
        }

        reader.close();
        return false;
    }

    private boolean lookForFile(SpringProject project, Map<String, String> properties, String fileName) {
        if (project.getPath() == null) {
            return false;
        }

        Path projectPath = Paths.get(project.getPath());
        Path filePath = projectPath.resolve(fileName);
        if (Files.exists(filePath)) {
            properties.put(fileName, filePath.toString());
            return true;
        }
        return false;
    }

    private void introspectJavaFiles(SpringProject project) {
        if (project.getPath() == null) {
            return;
        }

        // Create a JavaParser instance
        JavaParser javaParser = new JavaParser();

        // Get the project path
        Path projectPath = Paths.get(project.getPath());

        // Walk the project directory and parse each .java file
        try (Stream<Path> paths = Files.walk(projectPath)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> parseAndVisit(javaParser, path.toFile()));
        } catch (IOException e) {
            LOGGER.severe("Failed to walk Spring project path '" + projectPath + "' relative to working directory.");
//            e.printStackTrace();
        }
    }

    private void parseAndVisit(JavaParser javaParser, File file) {
        try {
            // Parse the .java file
            CompilationUnit cu = javaParser.parse(file).getResult().orElse(null);

            if (cu != null) {
                // Visit the parsed file
                cu.accept(new ValueAnnotationVisitor(), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ValueAnnotationVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(SingleMemberAnnotationExpr n, Void arg) {
            super.visit(n, arg);
            checkForValueAnnotation(n);
        }

        @Override
        public void visit(NormalAnnotationExpr n, Void arg) {
            super.visit(n, arg);
            checkForValueAnnotation(n);
        }

        @Override
        public void visit(MarkerAnnotationExpr n, Void arg) {
            super.visit(n, arg);
            checkForValueAnnotation(n);
        }

        private void checkForValueAnnotation(AnnotationExpr n) {
            // Check for @Value annotations
            if (n.getNameAsString().equals("Value")) {
                // TODO: Do something with the @Value annotation
                LOGGER.info("@Value annotation found: " + n);
            }
        }
    }

}
