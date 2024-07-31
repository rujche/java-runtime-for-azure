package com.azure.runtime.host.dcp.utils;

import java.util.Objects;

public class Version implements Comparable<Version> {
    private int major;
    private int minor;
    private int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public Version(String versionString) {
        String[] parts = versionString.split("\\.");
        if (parts.length > 0) {
            this.major = Integer.parseInt(parts[0]);
        }
        if (parts.length > 1) {
            this.minor = Integer.parseInt(parts[1]);
        }
        if (parts.length > 2) {
            this.patch = Integer.parseInt(parts[2]);
        }
        if (parts.length > 3) {
            throw new IllegalArgumentException("Invalid version string: " + versionString);
        }
        
    }
    
    // Getters
    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    // toString method for displaying version
    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    // compareTo method for comparing versions
    @Override
    public int compareTo(Version other) {
        if (this.major != other.major) {
            return Integer.compare(this.major, other.major);
        }
        if (this.minor != other.minor) {
            return Integer.compare(this.minor, other.minor);
        }
        return Integer.compare(this.patch, other.patch);
    }

    // equals method for comparing equality
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Version version = (Version) obj;
        return major == version.major && minor == version.minor &&
               patch == version.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }
}
