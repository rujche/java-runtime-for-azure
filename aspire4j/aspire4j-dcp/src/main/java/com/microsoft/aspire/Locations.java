package com.microsoft.aspire;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Locations {
    private String basePath;
    
    public String getDcpSessionDir() {
        return getOrCreateBasePath();
    }

    public String getDcpKubeconfigPath() {
        return Paths.get(getDcpSessionDir(), "kubeconfig").toString();
    }

    public String getDcpLogSocket() {
        return Paths.get(getDcpSessionDir(), "output.sock").toString();
    }

    public Locations() {
    }

    public Locations(String basePath) {
        this.basePath = basePath;
    }
    
    private String getOrCreateBasePath() {
        if (basePath == null) {
            try {
                basePath = Files.createTempDirectory("aspire.").toString();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create temp directory", e);
            }
        }
        return basePath;
    }
}
