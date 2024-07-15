package com.microsoft.aspire.model.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class EndpointSpec {
    @JsonProperty("serviceNamespace")
    private String serviceNamespace;

    @JsonProperty("serviceName")
    private String serviceName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("port")
    private Integer port;
}



