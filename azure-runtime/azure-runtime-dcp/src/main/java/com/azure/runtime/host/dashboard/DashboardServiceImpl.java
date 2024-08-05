package com.azure.runtime.host.dashboard;

import io.grpc.stub.StreamObserver;
import proto.DashboardServiceGrpc;
import proto.ResourceService;

public class DashboardServiceImpl extends DashboardServiceGrpc.DashboardServiceImplBase {

    
    public DashboardServiceImpl() {
    }
    
    @Override
    public void getApplicationInformation(ResourceService.ApplicationInformationRequest request, StreamObserver<ResourceService.ApplicationInformationResponse> responseObserver) {
        ResourceService.ApplicationInformationResponse response = ResourceService.ApplicationInformationResponse.newBuilder()
                .setApplicationName("Local Dashboard")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void watchResources(ResourceService.WatchResourcesRequest request, StreamObserver<ResourceService.WatchResourcesUpdate> responseObserver) {
        ResourceService.InitialResourceData initialData = ResourceService.WatchResourcesUpdate.newBuilder().getInitialData();
        responseObserver.onNext(ResourceService.WatchResourcesUpdate.newBuilder().setInitialData(initialData).build());


        ResourceService.WatchResourcesChange change = ResourceService.WatchResourcesChange.newBuilder().setUpsert(
                ResourceService.Resource.newBuilder().setName("test").build()
        ).build();
        responseObserver.onNext(ResourceService.WatchResourcesUpdate.newBuilder()
                .setChanges(ResourceService.WatchResourcesChanges.newBuilder().addValue(change)).build());
        responseObserver.onCompleted();
    }
}
