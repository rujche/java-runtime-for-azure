package com.azure.runtime.host.dcp.model.common;

public interface IAnnotationHolder {
    void annotate(String annotationName, String value);

    <TValue> void annotateAsObjectList(String annotationName, TValue value);
}