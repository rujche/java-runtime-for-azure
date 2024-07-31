package com.azure.runtime.host.dcp.logging;

import java.util.concurrent.CompletableFuture;

public interface AsyncIterator<T> {
    
    boolean hasNext();
    
    CompletableFuture<T> next();
}