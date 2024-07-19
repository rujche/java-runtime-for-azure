package com.microsoft.aspire;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.microsoft.aspire.implementation.utils.json.CustomSerializerModifier;
import com.microsoft.aspire.implementation.utils.json.RelativePathModule;
import com.microsoft.aspire.resources.traits.IntrospectiveResource;
import com.microsoft.aspire.resources.traits.ResourceWithLifecycle;
import com.microsoft.aspire.resources.traits.ResourceWithTemplate;
import com.microsoft.aspire.utils.FileUtilities;
import com.microsoft.aspire.utils.templates.TemplateFileOutput;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

// Not public API
class AppProcessor {

    private static final Logger LOGGER = Logger.getLogger(AppProcessor.class.getName());

    private Path outputPath;

    void processApps(AppHost appHost, Path outputPath) {
        this.outputPath = outputPath;

        File outputDir = outputPath.toFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        FileUtilities.setOutputPath(outputPath);

        DistributedApplication app = new DistributedApplication();
        appHost.configureApplication(app);
        processTemplates(app, outputPath);
        introspectResources(app);

        writeManifestToFile(app);
    }

    void writeManifestToFile(DistributedApplication app) {
        ObjectMapper objectMapper = prepareObjectMapper(app);
        LOGGER.info("Writing manifest to file");
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(outputPath.toFile(), "aspire-apps.json"), app.manifest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("Manifest written to file");
    }

    private ObjectMapper prepareObjectMapper(DistributedApplication app) {
        if (app.manifest.isEmpty()) {
            LOGGER.info("No configuration received from AppHost...exiting");
            System.exit(-1);
        }

        // Jackson ObjectMapper is used to serialize the AspireManifest object to a JSON string,
        // and write to a file named "aspire-manifest.json".
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new CustomSerializerModifier());
        objectMapper.registerModule(module);
        objectMapper.registerModule(new RelativePathModule());

//        printAnnotations(System.out, app);

        return objectMapper;
    }

    private void processTemplates(DistributedApplication app, Path outputPath) {
        LOGGER.info("Processing templates...");
        app.manifest.getResources().values().stream()
            .filter(r -> r instanceof ResourceWithTemplate<?>)
            .map(r -> (ResourceWithTemplate<?>) r)
            .map(ResourceWithTemplate::processTemplate)
            .forEach(templateFiles -> templateFiles.forEach(this::writeTemplateFile));
        LOGGER.info("Templates processed");
    }

    private void writeTemplateFile(TemplateFileOutput templateFile) {
        try {
            Path path = Paths.get(outputPath.toString() + "/" + templateFile.filename());

            // ensure the parent directories exist
            Files.createDirectories(path.getParent());
            Files.write(path, templateFile.content().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void introspectResources(DistributedApplication app) {
        
        for (ResourceWithLifecycle resource : app.manifest.getResources().values()) {
            if (resource instanceof IntrospectiveResource) {
                ((IntrospectiveResource) resource).introspect();
            }
        }
        
    }

    private void printAnnotations(PrintStream out, DistributedApplication app) {
        app.manifest.getResources().values().forEach(resource -> {
            out.println("Resource: " + resource.getName());
            resource.getAnnotations().forEach(annotation -> {
                out.println("  Annotation: " + annotation);
            });
        });
    }
}
