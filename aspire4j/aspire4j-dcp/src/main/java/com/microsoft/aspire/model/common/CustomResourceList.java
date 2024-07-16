package com.microsoft.aspire.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kubernetes.client.openapi.models.V1ListMeta;

import java.util.List;

public final class CustomResourceList<T extends CustomResource> {

    @JsonProperty("metadata")
    private V1ListMeta metadata = new V1ListMeta();

    @JsonProperty("items")
    private List<T> items;

    public V1ListMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(V1ListMeta metadata) {
        this.metadata = metadata;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}