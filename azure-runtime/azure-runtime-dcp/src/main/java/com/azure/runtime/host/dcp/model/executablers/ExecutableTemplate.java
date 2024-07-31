package com.azure.runtime.host.dcp.model.executablers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azure.runtime.host.dcp.model.common.CustomResource;
import com.azure.runtime.host.dcp.model.common.IAnnotationHolder;
import com.azure.runtime.host.dcp.model.executable.ExecutableSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a template for creating executable objects, holding labels, annotations, and specifications.
 */
public class ExecutableTemplate implements IAnnotationHolder {

    /**
     * Labels to apply to child Executable objects.
     */
    @JsonProperty("labels")
    private Map<String, String> labels;

    /**
     * Annotations to apply to child Executable objects.
     */
    @JsonProperty("annotations")
    private Map<String, String> annotations;

    /**
     * Spec for the child Executable.
     */
    @JsonProperty("spec")
    private ExecutableSpec spec = new ExecutableSpec();

    /**
     * Adds or updates an annotation with a given name and value.
     * @param annotationName the name of the annotation.
     * @param value the value of the annotation.
     */
    public void annotate(String annotationName, String value) {
        if (annotations == null) {
            annotations = new HashMap<>();
        }
        annotations.put(annotationName, value);
    }

    /**
     * Adds or updates an annotation as a list of objects.
     * @param annotationName the name of the annotation.
     * @param value the value to be added to the list in the annotation.
     */
    public <TValue> void annotateAsObjectList(String annotationName, TValue value) {
        if (annotations == null) {
            annotations = new HashMap<>();
        }
        CustomResource.annotateAsObjectList(annotations, annotationName, value);
    }

    /**
     * Tries to retrieve an annotation as a list of objects.
     * @param annotationName the name of the annotation.
     * @param list the list to be filled with the retrieved objects.
     * @return true if the annotation exists and was successfully converted to a list, false otherwise.
     */
    public <TValue> boolean tryGetAnnotationAsObjectList(String annotationName, List<TValue> list) {
        return CustomResource.tryGetAnnotationAsObjectList(annotations, annotationName, list);
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Map<String, String> annotations) {
        this.annotations = annotations;
    }

    public ExecutableSpec getSpec() {
        return spec;
    }

    public void setSpec(ExecutableSpec spec) {
        this.spec = spec;
    }
}
