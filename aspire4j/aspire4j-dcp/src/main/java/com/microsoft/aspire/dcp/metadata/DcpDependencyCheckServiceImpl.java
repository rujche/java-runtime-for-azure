package com.microsoft.aspire.dcp.metadata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aspire.dcp.exceptions.DistributedApplicationException;
import com.microsoft.aspire.dcp.process.Pair;
import com.microsoft.aspire.dcp.process.ProcessResult;
import com.microsoft.aspire.dcp.process.ProcessSpec;
import com.microsoft.aspire.dcp.process.ProcessUtil;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DcpDependencyCheckServiceImpl implements DcpDependencyCheckService {

    private final ReentrantLock lock = new ReentrantLock();
    private final DcpOptions dcpOptions;
    private boolean checkDone = false;
    private DcpInfo dcpInfo;
    
    
    public DcpDependencyCheckServiceImpl(DcpOptions dcpOptions) {
        this.dcpOptions = dcpOptions;
    }

    @Override
    public CompletableFuture<DcpInfo> getDcpInfoAsync() {
        return CompletableFuture.supplyAsync(() -> {
            lock.lock();
            try {
                if (checkDone) {
                    return dcpInfo;
                }
                checkDone = true;

                String dcpPath = dcpOptions.getCliPath();
                String containerRuntime = dcpOptions.getContainerRuntime();

                if (!new File(dcpPath).exists()) {
                    throw new RuntimeException("The Aspire orchestration component is not installed at \"" + dcpPath + "\". The application cannot be run without it.");
                }

                AutoCloseable processDisposable = null;
                CompletableFuture<ProcessResult> task;
                StringBuilder outputStringBuilder = new StringBuilder();
                StringBuilder errorStringBuilder = new StringBuilder();

                try {
                    String arguments = "info";
                    if (containerRuntime != null && !containerRuntime.isEmpty()) {
                        arguments += " --container-runtime " + containerRuntime;
                    }

                    ProcessSpec processSpec = new ProcessSpec(dcpPath);
                    processSpec.setArguments(arguments);
                    processSpec.setOnOutputData(outputStringBuilder::append);
                    processSpec.setOnErrorData(errorStringBuilder::append);
                    processSpec.setThrowOnNonZeroReturnCode(false);

                    Pair<CompletableFuture<ProcessResult>, AutoCloseable> result = ProcessUtil.run(processSpec);
                    task = result.first();
                    processDisposable = result.second();
                    ProcessResult processResult;

                    // Disable timeout if DependencyCheckTimeout is set to zero or a negative value
                    if (dcpOptions.getDependencyCheckTimeout() > 0) {
                        processResult = task.get(dcpOptions.getDependencyCheckTimeout(), TimeUnit.SECONDS);
                    } else {
                        processResult = task.get();
                    }

                    if (processResult.getExitCode() != 0) {
                        throw new RuntimeException(String.format(
                                "Dependency check failed: 'dcp %s' returned exit code %d. %s\n%s",
                                arguments, processResult.getExitCode(), errorStringBuilder, outputStringBuilder
                        ));
                    }

                    // Parse the output as JSON
                    String output = outputStringBuilder.toString();
                    if (output.isEmpty()) {
                        return null; // Best effort
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    DcpInfo dcpInfo = objectMapper.readValue(output, DcpInfo.class);
                    if (dcpInfo == null) {
                        return null; // Best effort
                    }

                    ensureDcpVersion(dcpInfo);
                    ensureDcpContainerRuntime(dcpInfo);
                    this.dcpInfo = dcpInfo;
                    return dcpInfo;
                } catch (Exception ex) {
                    if (!(ex instanceof DistributedApplicationException)) {
                        throw new DistributedApplicationException(String.format(
                                "Dependency check failed: %s %s\n%s",
                                ex.getMessage(), errorStringBuilder, outputStringBuilder
                        ));
                    } else {
                        System.out.println(errorStringBuilder);
                        System.out.println(outputStringBuilder);
                        throw (DistributedApplicationException) ex;
                    }
                } finally {
                    if (processDisposable != null) {
                        try {
                            processDisposable.close();
                        } catch (Exception e) {
                            // Dispose (dcp info process termination) is best effort.
                        }
                    }
                }
            } finally {
                lock.unlock();
            }
        });
    }
    
    private void ensureDcpVersion(DcpInfo dcpInfo) {
        if (dcpInfo.getVersion() == null) {
            throw new DistributedApplicationException("The DCP version is not available.");
        }
    }
    
    private void ensureDcpContainerRuntime(DcpInfo dcpInfo) {
        if (dcpInfo.getContainers() == null) {
            throw new DistributedApplicationException("The DCP container runtime information is not available.");
        }
    }
}
