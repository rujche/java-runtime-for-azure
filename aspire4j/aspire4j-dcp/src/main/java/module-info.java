module com.microsoft.com.aspire4j.dcp {
    exports com.microsoft.aspire.dcp;

    requires transitive io.kubernetes.client.java;
    requires com.google.gson;
    requires okhttp3;
    requires io.kubernetes.client.java.api;
    requires com.fasterxml.jackson.databind;
    requires com.microsoft.aspire;
    requires com.microsoft.aspire.extensions.spring;
    requires java.logging;
    requires com.google.protobuf;
    requires com.google.common;

    opens com.microsoft.aspire.dcp.model.common to com.google.gson;
    opens com.microsoft.aspire.dcp.model.container to com.google.gson;
    opens com.microsoft.aspire.dcp.model.executable to com.google.gson;
    exports com.microsoft.aspire.dcp.logging;
    exports com.microsoft.aspire.dcp.k8s;
    exports com.microsoft.aspire.dcp.utils;
    exports com.microsoft.aspire.dcp.exceptions;
    exports com.microsoft.aspire.dcp.resource;
    exports com.microsoft.aspire.dcp.metadata;

}