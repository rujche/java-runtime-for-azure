package com.microsoft.aspire.dcp;

import java.util.concurrent.CompletableFuture;

// IDcpDependencyCheckService Interface
public interface DcpDependencyCheckService {
    
    CompletableFuture<DcpInfo> getDcpInfoAsync();
}