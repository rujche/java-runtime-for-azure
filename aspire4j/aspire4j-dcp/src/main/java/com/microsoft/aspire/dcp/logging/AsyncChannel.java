package com.microsoft.aspire.dcp.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncChannel<T> {
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public CompletableFuture<Void> produce(T item) {
        return CompletableFuture.runAsync(() -> {
            try {
                queue.put(item);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, executor);
    }

    public CompletableFuture<T> consume() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }, executor);
    }
    
    public CompletableFuture<List<T>> consumeAll() {
        return CompletableFuture.supplyAsync(() -> {
            var items = new ArrayList<T>();
            queue.drainTo(items);
            return items;
        }, executor);
    }
}