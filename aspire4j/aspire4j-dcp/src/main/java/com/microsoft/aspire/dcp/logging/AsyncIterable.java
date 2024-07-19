package com.microsoft.aspire.dcp.logging;

public interface AsyncIterable<T> {

    AsyncIterator<T> iterator();
}
