package com.azure.runtime.host.dashboard;

import io.grpc.ServerBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerBuilder.forPort(8999)
                .addService(new DashboardServiceImpl())
                .build()
                .start().awaitTermination();
        
    }
}