package com.microsoft.aspire.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class EnvVar {
    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;

}