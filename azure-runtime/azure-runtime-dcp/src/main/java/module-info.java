module com.azure.runtime.host.dcp {
    exports com.azure.runtime.host.dcp;

    requires com.azure.runtime.host;
    requires com.azure.runtime.host.extensions.spring;
    
    requires transitive io.kubernetes.client.java;
    requires com.google.gson;
    requires okhttp3;
    requires io.kubernetes.client.java.api;
    requires com.fasterxml.jackson.databind;
    requires java.logging;
    requires com.google.protobuf;

    opens com.azure.runtime.host.dcp.model.common to com.google.gson;
    opens com.azure.runtime.host.dcp.model.container to com.google.gson;
    opens com.azure.runtime.host.dcp.model.executable to com.google.gson;
    exports com.azure.runtime.host.dcp.logging;
    exports com.azure.runtime.host.dcp.k8s;
    exports com.azure.runtime.host.dcp.utils;
    exports com.azure.runtime.host.dcp.exceptions;
    exports com.azure.runtime.host.dcp.resource;
    exports com.azure.runtime.host.dcp.metadata;

}