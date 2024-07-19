package com.microsoft.aspire.dcp.logging;

import com.microsoft.aspire.dcp.k8s.IKubernetesService;
import com.microsoft.aspire.dcp.metadata.DcpVersion;
import com.microsoft.aspire.dcp.model.common.CustomResource;
import com.microsoft.aspire.dcp.model.common.Logs;
import com.microsoft.aspire.dcp.model.container.Container;
import com.microsoft.aspire.dcp.utils.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class ResourceLogSource<TResource extends CustomResource> implements AsyncIterable<List<LogEntry>> {
    private static final Logger logger = Logger.getLogger(ResourceLogSource.class.getName());
    private final IKubernetesService kubernetesService;
    private final Version dcpVersion;
    private final TResource resource;

    public ResourceLogSource(IKubernetesService kubernetesService, Version dcpVersion, TResource resource) {
        this.kubernetesService = kubernetesService;
        this.dcpVersion = dcpVersion;
        this.resource = resource;
    }


    @Override
    public AsyncIterator<List<LogEntry>> iterator() {
        return new AsyncIterator<>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public CompletableFuture<List<LogEntry>> next() {
                return CompletableFuture.supplyAsync(() -> getNextLogEntryList());
            }
        };
    }

    private List<LogEntry> getNextLogEntryList() {
//        var channel = Channel.CreateUnbounded<LogEntry>(new UnboundedChannelOptions
//        {
//            AllowSynchronousContinuations = false,
//                    SingleReader = true,
//                    SingleWriter = false
//        });
        
        var channel = new AsyncChannel<LogEntry>();

        var streamTasks = new ArrayList<CompletableFuture<Void>>();
        var timestamps = resource instanceof Container;  // Timestamps are available only for Containers as of Aspire P5.
        if (resource instanceof Container && dcpVersion != null && dcpVersion.compareTo(DcpVersion.MinimumVersionAspire_8_1) >= 0) {
            var startupStderrStream = kubernetesService.getLogStreamAsync(resource, Logs.STREAM_TYPE_STARTUP_STD_ERR, true, timestamps);
            var startupStdoutStream = kubernetesService.getLogStreamAsync(resource, Logs.STREAM_TYPE_STARTUP_STD_OUT, true, timestamps);

            var startupStdoutStreamTask = CompletableFuture.runAsync(() -> streamLogsAsync(startupStdoutStream, false, channel));
            streamTasks.add(startupStdoutStreamTask);

            var startupStderrStreamTask = CompletableFuture.runAsync(() -> streamLogsAsync(startupStderrStream, false, channel));
            streamTasks.add(startupStderrStreamTask);
        }

        var stdoutStream = kubernetesService.getLogStreamAsync(resource, Logs.STREAM_TYPE_STD_OUT, true, timestamps);
        var stderrStream = kubernetesService.getLogStreamAsync(resource, Logs.STREAM_TYPE_STD_ERR, true, timestamps);

        var stdoutStreamTask = CompletableFuture.runAsync(() -> streamLogsAsync(stdoutStream, false, channel));
        streamTasks.add(stdoutStreamTask);

        var stderrStreamTask = CompletableFuture.runAsync(() -> streamLogsAsync(stderrStream, true, channel));
        streamTasks.add(stderrStreamTask);

        CompletableFuture.runAsync(() -> waitForStreamsToCompleteAsync(streamTasks, channel));

        try {
            return channel.consumeAll().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitForStreamsToCompleteAsync(List<CompletableFuture<Void>> streamTasks, AsyncChannel<LogEntry> channel) {
        try {
            CompletableFuture.allOf(streamTasks.toArray(new CompletableFuture[0])).join();
//            channel.writer().complete();
        } catch (Exception e) {
//            channel.writer().completeExceptionally(e);
        }
    }

    private void streamLogsAsync(InputStream stream, boolean isError, AsyncChannel<LogEntry> channel) {
        try (var sr = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = sr.readLine()) != null) {
                try {
                    channel.produce(new LogEntry(line, isError)).get();   
                } catch (Exception e) {
                    logger.warning("Failed to write log entry to channel. Logs for " + resource.getKind() + " " + resource.getMetadata().getName() + " may be incomplete");
                    return;
                }
            }
        } catch (IOException e) {
//            if (!cancellationToken.isCancelled()) {
//                logger.error("Unexpected error happened when capturing logs for {} {}", resource.getKind(), resource.getMetadata().getName(), e);
//                channel.writer().completeExceptionally(e);
//            }
        }
    }


}