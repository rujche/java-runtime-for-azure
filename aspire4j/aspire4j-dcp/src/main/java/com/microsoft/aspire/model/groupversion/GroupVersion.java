package com.microsoft.aspire.model.groupversion;

public class GroupVersion {
    private String group;
    private String version;

    public GroupVersion(String group, String version) {
        this.group = group;
        this.version = version;
    }

    // Getters and setters
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return group + "/" + version;
    }
}


