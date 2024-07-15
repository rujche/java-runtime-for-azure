package com.microsoft.aspire.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public final class ServiceProducerAnnotation {
    @JsonProperty("serviceName")
    private String serviceName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("port")
    private Integer port;

    public ServiceProducerAnnotation(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ServiceProducerAnnotation other = (ServiceProducerAnnotation) obj;
        return Objects.equals(serviceName, other.serviceName) &&
               Objects.equals(address, other.address) &&
               Objects.equals(port, other.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, address, port);
    }
}
