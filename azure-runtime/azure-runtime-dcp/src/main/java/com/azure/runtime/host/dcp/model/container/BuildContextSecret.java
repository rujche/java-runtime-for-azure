package com.azure.runtime.host.dcp.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the details for a build context secret.
 */
public class BuildContextSecret {
    /**
     * The ID of the secret. A secret can be used in a Dockerfile with `RUN --mount-type=secret,id=<id>,target=<targetpath>`.
     */
    @JsonProperty("id")
    private String id;

    /**
     * The type of the secret, can be "env" or "file".
     */
    @JsonProperty("type")
    private String type;

    /**
     * The value of the secret to be used in the build when the type of the secret is "env".
     */
    @JsonProperty("value")
    private String value;

    /**
     * The path to secret file/folder that will be mounted as a build secret using --secret.
     */
    @JsonProperty("source")
    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}