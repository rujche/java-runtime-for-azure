package com.microsoft.aspire.dcp.utils;

import java.util.Objects;

public class Version implements Comparable<Version> {
    private int major;
    private int minor;
    private int patch;
    private int build;

    public Version(int major, int minor, int patch, int build) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.build = build;
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

    public int getBuild() {
        return build;
    }

    // toString method for displaying version
    @Override
    public String toString() {
        return major + "." + minor + "." + patch + "." + build;
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
        if (this.patch != other.patch) {
            return Integer.compare(this.patch, other.patch);
        }
        return Integer.compare(this.build, other.build);
    }

    // equals method for comparing equality
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Version version = (Version) obj;
        return major == version.major && minor == version.minor &&
               patch == version.patch && build == version.build;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, build);
    }

    public static void main(String[] args) {
        Version version1 = new Version(1, 0, 0, 0);
        Version version2 = new Version(1, 0, 1, 0);

        System.out.println("Version 1: " + version1);
        System.out.println("Version 2: " + version2);
        System.out.println("Comparison: " + version1.compareTo(version2));
    }
}
