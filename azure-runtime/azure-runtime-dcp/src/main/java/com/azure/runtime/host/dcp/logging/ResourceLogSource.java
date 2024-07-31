package com.azure.runtime.host.dcp.logging;

import com.azure.runtime.host.dcp.k8s.IKubernetesService;
import com.azure.runtime.host.dcp.metadata.DcpVersion;
import com.azure.runtime.host.dcp.model.common.CustomResource;
import com.azure.runtime.host.dcp.model.common.Logs;
import com.azure.runtime.host.dcp.model.container.Container;
import com.azure.runtime.host.dcp.utils.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ResourceLogSource<TResource extends CustomResource> implements AsyncIterable<List<LogEntry>>, AutoCloseable {
    private static final Logger logger = Logger.getLogger(ResourceLogSource.class.getName());
    private final IKubernetesService kubernetesService;
    private final Version dcpVersion;
    private final TResource resource;

    private static ExecutorService EXECUTORS = Executors.newFixedThreadPool(5);
    private AsyncChannel<List<LogEntry>> channel = new AsyncChannel<>(EXECUTORS);
    private List<CompletableFuture<Void>> streamTasks = new ArrayList<>();

    public ResourceLogSource(IKubernetesService kubernetesService, Version dcpVersion, TResource resource) {
        this.kubernetesService = kubernetesService;
        this.dcpVersion = dcpVersion;
        this.resource = resource;
        init();
    }
    
    private void init() {
        var timestamps = resource instanceof Container;  // Timestamps are available only for Containers as of Aspire P5.
        if (resource instanceof Container && dcpVersion != null && dcpVersion.compareTo(DcpVersion.MinimumVersionAspire_8_1) >= 0) {
            var startupStderrStream = kubernetesService.getLogStreamAsync(resource, Logs.STREAM_TYPE_STARTUP_STD_ERR, true, timestamps);
            var startupStdoutStream = kubernetesService.getLogStreamAsync(resource, Logs.STREAM_TYPE_STARTUP_STD_OUT, true, timestamps);

            var startupStdoutStreamTask = CompletableFuture.runAsync(() -> streamLogsAsync(startupStdoutStream, false, channel));
            streamTasks.add(startupStdoutStreamTask);

            var startupStderrStreamTask = CompletableFuture.runAsync(() -> streamLogsAsync(startupStderrStream, true, channel));
            streamTasks.add(startupStderrStreamTask);
        }

        var stdoutStream = kubernetesService.getLogStreamAsync(resource, Logs.STREAM_TYPE_STD_OUT, true, timestamps);
        var stderrStream = kubernetesService.getLogStreamAsync(resource, Logs.STREAM_TYPE_STD_ERR, true, timestamps);

        var stdoutStreamTask = CompletableFuture.runAsync(() -> streamLogsAsync(stdoutStream, false, channel));
        streamTasks.add(stdoutStreamTask);

        var stderrStreamTask = CompletableFuture.runAsync(() -> streamLogsAsync(stderrStream, true, channel));
        streamTasks.add(stderrStreamTask);
        
    }

    private void waitForStreamsToCompleteAsync(List<CompletableFuture<Void>> streamTasks, AsyncChannel<List<LogEntry>> channel) {
        try {
            CompletableFuture.allOf(streamTasks.toArray(new CompletableFuture[0])).join();
//            channel.writer().complete();
        } catch (Exception e) {
//            channel.writer().completeExceptionally(e);
        }
    }

    private void streamLogsAsync(InputStream stream, boolean isError, AsyncChannel<List<LogEntry>> channel) {
        try (var sr = new BufferedReader(new InputStreamReader(stream))) {
            String line;
//            List<String> buffer = new ArrayList<>(20);
//            long startTime = System.currentTimeMillis();

            while ((line = sr.readLine()) != null) {
                var logs = List.of(new LogEntry(line, isError));
                try {
                    channel.produce(logs).get();
                } catch (Exception e) {
                    logger.warning("Failed to write log entry to channel. Logs for " + resource.getKind() + " " + resource.getMetadata().getName() + " may be incomplete");
                }

//                buffer.add(line);
//                if (buffer.size() == 20 || System.currentTimeMillis() - startTime > 1000) {
//                    try {
//                        var logs = buffer.stream().map(l -> new LogEntry(l, isError)).toList();
//                        channel.produce(logs).get();
//                    } catch (Exception e) {
//                        logger.warning("Failed to write log entry to channel. Logs for " + resource.getKind() + " " + resource.getMetadata().getName() + " may be incomplete");
//                        return;
//                    }
//                    buffer.clear();
//                    startTime = System.currentTimeMillis();
//                }
            }
            
//            if (!buffer.isEmpty()) {
//                try {
//                    var logs = buffer.stream().map(l -> new LogEntry(l, isError)).toList();
//                    channel.produce(logs).get();
//                } catch (Exception e) {
//                    logger.warning("Failed to write log entry to channel. Logs for " + resource.getKind() + " " + resource.getMetadata().getName() + " may be incomplete");
//                }
//            }
        } catch (IOException e) {
//            if (!cancellationToken.isCancelled()) {
//                logger.error("Unexpected error happened when capturing logs for {} {}", resource.getKind(), resource.getMetadata().getName(), e);
//                channel.writer().completeExceptionally(e);
//            }
        }
    }


    @Override
    public void close() throws Exception {
        CompletableFuture.runAsync(() -> waitForStreamsToCompleteAsync(streamTasks, channel));
        channel.consumeAll().get();
    }

    @Override
    public AsyncIterator<List<LogEntry>> iterator() {
        return channel.iterator();
    }
}