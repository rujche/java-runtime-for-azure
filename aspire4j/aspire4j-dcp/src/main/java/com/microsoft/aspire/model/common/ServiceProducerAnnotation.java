package com.microsoft.aspire.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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
