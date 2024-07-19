package com.microsoft.aspire.dcp.logging;

import java.util.concurrent.CompletableFuture;

public interface AsyncIterator<T> {
    
    boolean hasNext();
    
    CompletableFuture<T> next();
}