package com.microsoft.aspire.dcp.metadata;

import java.util.concurrent.CompletableFuture;

// IDcpDependencyCheckService Interface
public interface DcpDependencyCheckService {
    
    CompletableFuture<DcpInfo> getDcpInfoAsync();
}