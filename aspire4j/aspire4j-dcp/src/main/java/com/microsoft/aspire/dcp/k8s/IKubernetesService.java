package com.microsoft.aspire.dcp.k8s;

import io.kubernetes.client.util.Watch;
import com.microsoft.aspire.dcp.model.common.CustomResource;

import java.io.InputStream;
import java.util.List;

public interface IKubernetesService {

    <T extends CustomResource> T get(Class<T> clazz, String name, String namespaceParameter);

    <T extends CustomResource> T create(Class<T> clazz, T obj);

    <T extends CustomResource> List<T> list(Class<T> clazz, String namespaceParameter);

    <T extends CustomResource> T delete(Class<T> clazz, String name, String namespaceParameter);

    <T extends CustomResource> Watch<T> watch(Class<T> clazz, String namespaceParameter);

    <T extends CustomResource> InputStream getLogStreamAsync(T obj, String logStreamType, boolean follow, boolean timestamps);
}
