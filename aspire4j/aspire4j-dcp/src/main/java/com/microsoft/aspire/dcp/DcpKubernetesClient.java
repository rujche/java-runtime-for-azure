package com.microsoft.aspire.dcp;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.Streams;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DcpKubernetesClient extends ApiClient {

    public DcpKubernetesClient() {
        super();
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

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme(this.getBasePath().startsWith("https") ? "https" : "http")
                .host(this.getBasePath())
                .addPathSegments("apis/" + group + "/" + version + (namespaceParameter != null ? "/namespaces/" + namespaceParameter : "") + "/" + plural + "/" + name + "/" + subResource);

        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();

        OkHttpClient client = this.getHttpClient().newBuilder()
                .readTimeout(timeout, timeUnit)
                .build();

        Call call = client.newCall(request);
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
