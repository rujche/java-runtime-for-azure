package com.azure.runtime.host.dcp.logging;

public interface AsyncIterable<T> {

    AsyncIterator<T> iterator();
}
