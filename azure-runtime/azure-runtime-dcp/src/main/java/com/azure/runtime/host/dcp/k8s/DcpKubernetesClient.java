package com.azure.runtime.host.dcp.k8s;

import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Pair;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DcpKubernetesClient extends ApiClient {

    private final ApiClient delegate;
    
    public DcpKubernetesClient(ApiClient apiClient) {
        this.delegate = apiClient;
    }
    
    public ApiClient getDelegate() {
        return delegate;
    }

//    public DcpKubernetesClient(OkHttpClient httpClient, String basePath) {
//        super(httpClient, basePath);
//    }
//
//    public DcpKubernetesClient(String basePath) {
//        super(basePath);
//    }

    public InputStream readSubResourceAsStreamAsync(
            String group,
            String version,
            String plural,
            String name,
            String subResource,
            String namespaceParameter,
            Map<String, String> queryParams,
            // FIXME not null
            /*@NotNull*/ TimeUnit timeUnit,
            long timeout) throws ApiException, IOException {

        if (group == null || group.isEmpty()) throw new IllegalArgumentException("Group cannot be null or empty");
        if (version == null || version.isEmpty()) throw new IllegalArgumentException("Version cannot be null or empty");
        if (plural == null || plural.isEmpty()) throw new IllegalArgumentException("Plural cannot be null or empty");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be null or empty");
        if (subResource == null || subResource.isEmpty()) throw new IllegalArgumentException("SubResource cannot be null or empty");

        String pathSegments = "/apis/" + group + "/" + version + (namespaceParameter != null ? "/namespaces/" + namespaceParameter : "") + "/" + plural + "/" + name + "/" + subResource;

        List<Pair> localVarQueryParams = new ArrayList<>();
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                localVarQueryParams.addAll(this.delegate.parameterToPair(entry.getKey(), entry.getValue()));
            }
        }

        List<Pair> localVarCollectionQueryParams = new ArrayList();
        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"text/plain", "application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.delegate.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.delegate.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }
        String[] localVarAuthNames = new String[]{"BearerToken"};

        Call call = this.delegate.buildCall(null, pathSegments, "GET", localVarQueryParams, localVarCollectionQueryParams,
                null, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, new ApiCallback() {
                    @Override
                    public void onFailure(ApiException e, int statusCode, Map responseHeaders) {

                    }

                    @Override
                    public void onSuccess(Object result, int statusCode, Map responseHeaders) {

                    }

                    @Override
                    public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                    }

                    @Override
                    public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                    }
                });


        Response response = call.execute();
        if (!response.isSuccessful()) {
            throw new ApiException(response.message());
        }

        return response.body().byteStream();
    }

    private static class QueryBuilder {
        private final StringBuilder query = new StringBuilder();

        public void append(String key, int val) {
            if (query.length() == 0) query.append("?");
            else query.append("&");
            query.append(key).append("=").append(val);
        }

        public void append(String key, boolean val) {
            if (query.length() == 0) query.append("?");
            else query.append("&");
            query.append(key).append("=").append(val);
        }

        public void append(String key, String val) {
            if (query.length() == 0) query.append("?");
            else query.append("&");
            query.append(key).append("=").append(val);
        }

        @Override
        public String toString() {
            return query.toString();
        }
    }
}
