module com.microsoft.com.aspire4j.dcp {
    exports com.microsoft.aspire.dcp;

    requires io.kubernetes.client.java;
    requires com.google.gson;
    requires okhttp3;
    requires io.kubernetes.client.java.api;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;
    requires com.microsoft.aspire;
    
    opens com.microsoft.aspire.dcp.model.common to com.google.gson;
    opens com.microsoft.aspire.dcp.model.container to com.google.gson;

}