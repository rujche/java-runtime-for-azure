package com.microsoft.aspire.dcp.model.common;

public interface IAnnotationHolder {
    void annotate(String annotationName, String value);

    <TValue> void annotateAsObjectList(String annotationName, TValue value);
}