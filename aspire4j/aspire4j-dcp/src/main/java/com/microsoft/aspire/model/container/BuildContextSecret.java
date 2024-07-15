package com.microsoft.aspire.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the details for a build context secret.
 */
@Getter
@Setter
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
}