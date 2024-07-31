package com.azure.runtime.host.dcp.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncChannel<T> implements AsyncIterable<T>{
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executor;
    
    public AsyncChannel() {
        this(Executors.newCachedThreadPool());
    }
    
    public AsyncChannel(ExecutorService executor) {
        this.executor = executor;
    }

    public CompletableFuture<Void> produce(T item) {
        System.out.println("Producing on thread " + Thread.currentThread().getName() + " item " + item);
        try {
            queue.put(item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<T> consume() {
        System.out.println("Consuming on thread " + Thread.currentThread().getName());
        T take;
        try {
            take = queue.take();
            return CompletableFuture.completedFuture(take);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public CompletableFuture<List<T>> consumeAll() {
        return CompletableFuture.supplyAsync(() -> {

            List<T> items = new ArrayList<>();
            while (items.isEmpty()) {
                queue.drainTo(items);
                if (items.isEmpty()) {
                    synchronized (queue) {
                        if (queue.isEmpty()) {
                            try {
                                queue.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
            return items;
        }, executor);
    }

    @Override
    public AsyncIterator<T> iterator() {
        return new AsyncIterator<>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public CompletableFuture<T> next() {
                return consume();
            }
        };
    }
}