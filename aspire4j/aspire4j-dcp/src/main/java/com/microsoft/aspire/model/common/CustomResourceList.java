package com.microsoft.aspire.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kubernetes.client.openapi.models.V1ListMeta;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public final class CustomResourceList<T extends CustomResource> {

    @JsonProperty("metadata")
    private V1ListMeta metadata = new V1ListMeta();

    @JsonProperty("items")
    private List<T> items;

}