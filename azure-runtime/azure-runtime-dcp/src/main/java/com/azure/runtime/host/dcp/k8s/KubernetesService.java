package com.azure.runtime.host.dcp.k8s;

import com.google.gson.JsonElement;
import com.azure.runtime.host.dcp.metadata.DcpOptions;
import com.azure.runtime.host.dcp.metadata.Locations;
import com.azure.runtime.host.dcp.model.Schema;
import com.azure.runtime.host.dcp.model.common.CustomResource;
import com.azure.runtime.host.dcp.model.common.Logs;
import com.azure.runtime.host.dcp.model.groupversion.Dcp;
import com.azure.runtime.host.dcp.model.groupversion.GroupVersion;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.ApiResponse;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.JSON;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class KubernetesService implements IKubernetesService, AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(KubernetesService.class.getName());
    private static final Duration INITIAL_RETRY_DELAY = Duration.ofMillis(100);
    private static final GroupVersion GROUP_VERSION = Dcp.GROUP_VERSION;
    private final Semaphore kubeconfigReadSemaphore = new Semaphore(1);

    private DcpKubernetesClient kubernetes;
    // FIXME: resiliencePipeline
//    private ResiliencePipeline resiliencePipeline;
    private boolean disposed;
    private final DcpOptions dcpOptions;
    private final Locations locations;

    public KubernetesService(DcpOptions dcpOptions, Locations locations) {
        this.dcpOptions = dcpOptions;
        this.locations = locations;
    }

    public void init(Locations locations) {
        if (this.kubernetes != null) {
            return;
        }
        DcpKubernetesClient dcpKubernetesClient = null;
        try {
            // FIXME wait until the dcp processes are available
            Thread.sleep(Duration.ofSeconds(5));
            ApiClient client = Config.fromConfig(new FileInputStream(locations.getDcpKubeconfigPath()));
            dcpKubernetesClient = new DcpKubernetesClient(client);
            Configuration.setDefaultApiClient(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.kubernetes = dcpKubernetesClient;
    }

    private Duration maxRetryDuration = Duration.ofSeconds(20);


    @Override
    public void close() throws Exception {

    }

    @Override
    public <T extends CustomResource> T get(Class<T> clazz, String name, String namespaceParameter) {
        init(this.locations);
        String resourceType = getResourceFor(clazz);
        CustomObjectsApi apiInstance = new CustomObjectsApi(this.getApiClient());
        String group = GROUP_VERSION.getGroup();
        String version = GROUP_VERSION.getVersion();
        boolean namespaceNotProvided = isNullOrEmpty(namespaceParameter);
        try {
            if (namespaceNotProvided) {
                ApiResponse<Object> result = apiInstance.getClusterCustomObject(group, version, resourceType, name)
                    .executeWithHttpInfo();
                return convert(result.getData(), clazz);
            } else {
                ApiResponse<Object> result = apiInstance.getNamespacedCustomObject(group, version, namespaceParameter, resourceType, name)
                    .executeWithHttpInfo();
                return convert(result.getData(), clazz);
            }
        } catch (ApiException e) {
            logApiException(e, namespaceNotProvided ? "CustomObjectsApi#getClusterCustomObject" : "CustomObjectsApi#getNamespacedCustomObject");
            return null;
        }
    }

    @Override
    public <T extends CustomResource> T create(Class<T> clazz, T obj) {
        init(this.locations);
        String namespace = obj.getMetadata() == null ? null : obj.getMetadata().getNamespace();
        String resourceType = getResourceFor(clazz);

        CustomObjectsApi apiInstance = new CustomObjectsApi(this.getApiClient());
        String group = GROUP_VERSION.getGroup();
        String version = GROUP_VERSION.getVersion();
//        String pretty = "pretty_example"; // String | If 'true', then the output is pretty printed.
//        String dryRun = "dryRun_example"; // String | When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
//        String fieldManager = "fieldManager_example"; // String | fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
//        String fieldValidation = "fieldValidation_example"; // String | fieldValidation instructs the server on how to handle objects in the request (POST/PUT/PATCH) containing unknown or duplicate fields. Valid values are: - Ignore: This will ignore any unknown fields that are silently dropped from the object, and will ignore all but the last duplicate field that the decoder encounters. This is the default behavior prior to v1.23. - Warn: This will send a warning via the standard warning response header for each unknown field that is dropped from the object, and for each duplicate field that is encountered. The request will still succeed if there are no other errors, and will only persist the last of any duplicate fields. This is the default in v1.23+ - Strict: This will fail the request with a BadRequest error if any unknown fields would be dropped from the object, or if any duplicate fields are present. The error returned from the server will contain all unknown and duplicate fields encountered. (optional)
        boolean namespaceNotProvided = isNullOrEmpty(namespace);
        try {
            if (namespaceNotProvided) {
                ApiResponse<Object> result = apiInstance.createClusterCustomObject(group, version, resourceType, obj)
//                        .pretty(pretty)
//                        .dryRun(dryRun)
//                        .fieldManager(fieldManager)
//                        .fieldValidation(fieldValidation)
                        .executeWithHttpInfo();
                return convert(result.getData(), clazz);
            } else {
                ApiResponse<Object> result = apiInstance.createNamespacedCustomObject(group, version, namespace, resourceType, obj)
//                        .pretty(pretty)
//                        .dryRun(dryRun)
//                        .fieldManager(fieldManager)
//                        .fieldValidation(fieldValidation)
                        .executeWithHttpInfo();
                return convert(result.getData(), clazz);
            }
            
        } catch (ApiException e) {
            logApiException(e, namespaceNotProvided ? "CustomObjectsApi#createClusterCustomObject" : "CustomObjectsApi#createNamespacedCustomObject");
            return null;
        }
    }

    @Override
    public <T extends CustomResource> List<T> list(Class<T> clazz, String namespaceParameter) {
        init(this.locations);
        String resourceType = getResourceFor(clazz);
        CustomObjectsApi apiInstance = new CustomObjectsApi(this.getApiClient());
        String group = GROUP_VERSION.getGroup();
        String version = GROUP_VERSION.getVersion();
        
        boolean namespaceNotProvided = isNullOrEmpty(namespaceParameter);
        try {
            if (namespaceNotProvided) {
                ApiResponse<Object> result = apiInstance.listClusterCustomObject(group, version, resourceType)
                    .executeWithHttpInfo();
                return convert(result.getData(), List.class);
            } else {
                ApiResponse<Object> result = apiInstance.listNamespacedCustomObject(group, version, namespaceParameter, resourceType)
                    .executeWithHttpInfo();
                return convert(result.getData(), List.class);
            }
        } catch (ApiException e) {
            logApiException(e, namespaceNotProvided ? "CustomObjectsApi#listClusterCustomObject" : "CustomObjectsApi#listNamespacedCustomObject");
            return null;
        }
    }

    @Override
    public <T extends CustomResource> T delete(Class<T> clazz, String name, String namespaceParameter) {
        init(this.locations);
        String resourceType = getResourceFor(clazz);
        CustomObjectsApi apiInstance = new CustomObjectsApi(this.getApiClient());
        String group = GROUP_VERSION.getGroup();
        String version = GROUP_VERSION.getVersion();
        boolean namespaceNotProvided = isNullOrEmpty(namespaceParameter);
        try {
            if (namespaceNotProvided) {
                ApiResponse<Object> result = apiInstance.deleteClusterCustomObject(group, version, resourceType, name)
                    .executeWithHttpInfo();
                return convert(result.getData(), clazz);
            } else {
                ApiResponse<Object> result = apiInstance.deleteNamespacedCustomObject(group, version, namespaceParameter, resourceType, name)
                    .executeWithHttpInfo();
                return convert(result.getData(), clazz);
            }
        } catch (ApiException e) {
            logApiException(e, namespaceNotProvided ? "CustomObjectsApi#deleteClusterCustomObject" : "CustomObjectsApi#deleteNamespacedCustomObject");
        }
        return null;
    }

    // FIXME: watch
    @Override
    public <T extends CustomResource> Watch<T> watch(Class<T> clazz, String namespaceParameter, Type watchType) {
        init(this.locations);
        String resourceType = getResourceFor(clazz);
        CustomObjectsApi apiInstance = new CustomObjectsApi(this.getApiClient());
        String group = GROUP_VERSION.getGroup();
        String version = GROUP_VERSION.getVersion();
        
        boolean namespaceNotProvided = isNullOrEmpty(namespaceParameter);
        try {
            if (namespaceNotProvided) {
                return Watch.createWatch(this.getApiClient(), apiInstance.listClusterCustomObject(group, version, resourceType)
                                .watch(true)
                        .executeAsync(new ApiCallback<Object>() {
                            @Override
                            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                                
                            }

                            @Override
                            public void onSuccess(Object result, int statusCode, Map<String, List<String>> responseHeaders) {
                                
                            }

                            @Override
                            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
                                
                            }

                            @Override
                            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
                                
                            }
                        }).clone(), watchType);
            } else {
                return Watch.createWatch(this.getApiClient(), apiInstance
                        .listNamespacedCustomObject(group, version, namespaceParameter, resourceType)
                        .watch(true)
                        .executeAsync(new ApiCallback<Object>() {
                            @Override
                            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                                
                            }

                            @Override
                            public void onSuccess(Object result, int statusCode, Map<String, List<String>> responseHeaders) {

                            }

                            @Override
                            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                            }

                            @Override
                            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                            }
                        }), watchType);
            }
        } catch (ApiException e) {
            logApiException(e, namespaceParameter == null ? "CustomObjectsApi#listClusterCustomObject" : "CustomObjectsApi#listNamespacedCustomObject");
        }
        // FIXME: return null
        return null;
    }

    @Override
    public <T extends CustomResource> InputStream getLogStreamAsync(T obj, String logStreamType, boolean follow, boolean timestamps) {
        init(this.locations);
        String resourceType = getResourceFor(obj.getClass());
        String group = GROUP_VERSION.getGroup();
        String version = GROUP_VERSION.getVersion();
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("follow", String.valueOf(follow));
        queryParams.put("timestamps", String.valueOf(timestamps));
        queryParams.put("source", logStreamType);

        try {
            return this.kubernetes.readSubResourceAsStreamAsync(group, version, resourceType, 
                    obj.getMetadata().getName(), Logs.SUB_RESOURCE_NAME, obj.getMetadata().getNamespace(), queryParams, TimeUnit.SECONDS, 10);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        try {
////            return apiInstance.getApiClient()(group, version, obj.getMetadata().getNamespace(), resourceType, obj.getMetadata().getName(), logStreamType, follow, timestamps);
//        } catch (ApiException e) {
//            logApiException(e, "CustomObjectsApi#getNamespacedCustomObjectLogStream");
//        }
    }

    private static <T extends CustomResource> String getResourceFor(Class<T> resourceClass) {
        Schema.ResourceDetails resourceDetails = Dcp.SCHEMA.get(resourceClass);
        if (resourceDetails == null) {
            throw new IllegalArgumentException("Unknown custom resource type: " + resourceClass.getSimpleName());
        }
        return resourceDetails.getResource();
    }

    private static <T> T convert(Object o, Class<T> clazz) {
        if (o == null) return null;
        JsonElement jsonElement = JSON.getGson().toJsonTree(o);
        return JSON.getGson().fromJson(jsonElement, clazz);
    }
    
    private static void logApiException(ApiException e, String method) {
        LOGGER.severe("Exception when calling " + method);
        LOGGER.severe("Status code: " + e.getCode());
        LOGGER.severe("Reason: " + e.getResponseBody());
        LOGGER.severe("Response headers: " + e.getResponseHeaders());
        for (StackTraceElement ste : e.getStackTrace()) {
            LOGGER.severe(ste.toString());
        }
    }
    
    private static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
    private ApiClient getApiClient() {
        return this.kubernetes.getDelegate();
    }

}