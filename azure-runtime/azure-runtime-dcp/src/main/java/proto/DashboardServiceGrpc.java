package proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.57.1)",
    comments = "Source: resource_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DashboardServiceGrpc {

  private DashboardServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "DashboardService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto.ResourceService.ApplicationInformationRequest,
      proto.ResourceService.ApplicationInformationResponse> getGetApplicationInformationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetApplicationInformation",
      requestType = proto.ResourceService.ApplicationInformationRequest.class,
      responseType = proto.ResourceService.ApplicationInformationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.ResourceService.ApplicationInformationRequest,
      proto.ResourceService.ApplicationInformationResponse> getGetApplicationInformationMethod() {
    io.grpc.MethodDescriptor<proto.ResourceService.ApplicationInformationRequest, proto.ResourceService.ApplicationInformationResponse> getGetApplicationInformationMethod;
    if ((getGetApplicationInformationMethod = DashboardServiceGrpc.getGetApplicationInformationMethod) == null) {
      synchronized (DashboardServiceGrpc.class) {
        if ((getGetApplicationInformationMethod = DashboardServiceGrpc.getGetApplicationInformationMethod) == null) {
          DashboardServiceGrpc.getGetApplicationInformationMethod = getGetApplicationInformationMethod =
              io.grpc.MethodDescriptor.<proto.ResourceService.ApplicationInformationRequest, proto.ResourceService.ApplicationInformationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetApplicationInformation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.ResourceService.ApplicationInformationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.ResourceService.ApplicationInformationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DashboardServiceMethodDescriptorSupplier("GetApplicationInformation"))
              .build();
        }
      }
    }
    return getGetApplicationInformationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.ResourceService.WatchResourcesRequest,
      proto.ResourceService.WatchResourcesUpdate> getWatchResourcesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "WatchResources",
      requestType = proto.ResourceService.WatchResourcesRequest.class,
      responseType = proto.ResourceService.WatchResourcesUpdate.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<proto.ResourceService.WatchResourcesRequest,
      proto.ResourceService.WatchResourcesUpdate> getWatchResourcesMethod() {
    io.grpc.MethodDescriptor<proto.ResourceService.WatchResourcesRequest, proto.ResourceService.WatchResourcesUpdate> getWatchResourcesMethod;
    if ((getWatchResourcesMethod = DashboardServiceGrpc.getWatchResourcesMethod) == null) {
      synchronized (DashboardServiceGrpc.class) {
        if ((getWatchResourcesMethod = DashboardServiceGrpc.getWatchResourcesMethod) == null) {
          DashboardServiceGrpc.getWatchResourcesMethod = getWatchResourcesMethod =
              io.grpc.MethodDescriptor.<proto.ResourceService.WatchResourcesRequest, proto.ResourceService.WatchResourcesUpdate>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "WatchResources"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.ResourceService.WatchResourcesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.ResourceService.WatchResourcesUpdate.getDefaultInstance()))
              .setSchemaDescriptor(new DashboardServiceMethodDescriptorSupplier("WatchResources"))
              .build();
        }
      }
    }
    return getWatchResourcesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.ResourceService.WatchResourceConsoleLogsRequest,
      proto.ResourceService.WatchResourceConsoleLogsUpdate> getWatchResourceConsoleLogsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "WatchResourceConsoleLogs",
      requestType = proto.ResourceService.WatchResourceConsoleLogsRequest.class,
      responseType = proto.ResourceService.WatchResourceConsoleLogsUpdate.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<proto.ResourceService.WatchResourceConsoleLogsRequest,
      proto.ResourceService.WatchResourceConsoleLogsUpdate> getWatchResourceConsoleLogsMethod() {
    io.grpc.MethodDescriptor<proto.ResourceService.WatchResourceConsoleLogsRequest, proto.ResourceService.WatchResourceConsoleLogsUpdate> getWatchResourceConsoleLogsMethod;
    if ((getWatchResourceConsoleLogsMethod = DashboardServiceGrpc.getWatchResourceConsoleLogsMethod) == null) {
      synchronized (DashboardServiceGrpc.class) {
        if ((getWatchResourceConsoleLogsMethod = DashboardServiceGrpc.getWatchResourceConsoleLogsMethod) == null) {
          DashboardServiceGrpc.getWatchResourceConsoleLogsMethod = getWatchResourceConsoleLogsMethod =
              io.grpc.MethodDescriptor.<proto.ResourceService.WatchResourceConsoleLogsRequest, proto.ResourceService.WatchResourceConsoleLogsUpdate>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "WatchResourceConsoleLogs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.ResourceService.WatchResourceConsoleLogsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.ResourceService.WatchResourceConsoleLogsUpdate.getDefaultInstance()))
              .setSchemaDescriptor(new DashboardServiceMethodDescriptorSupplier("WatchResourceConsoleLogs"))
              .build();
        }
      }
    }
    return getWatchResourceConsoleLogsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.ResourceService.ResourceCommandRequest,
      proto.ResourceService.ResourceCommandResponse> getExecuteResourceCommandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ExecuteResourceCommand",
      requestType = proto.ResourceService.ResourceCommandRequest.class,
      responseType = proto.ResourceService.ResourceCommandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.ResourceService.ResourceCommandRequest,
      proto.ResourceService.ResourceCommandResponse> getExecuteResourceCommandMethod() {
    io.grpc.MethodDescriptor<proto.ResourceService.ResourceCommandRequest, proto.ResourceService.ResourceCommandResponse> getExecuteResourceCommandMethod;
    if ((getExecuteResourceCommandMethod = DashboardServiceGrpc.getExecuteResourceCommandMethod) == null) {
      synchronized (DashboardServiceGrpc.class) {
        if ((getExecuteResourceCommandMethod = DashboardServiceGrpc.getExecuteResourceCommandMethod) == null) {
          DashboardServiceGrpc.getExecuteResourceCommandMethod = getExecuteResourceCommandMethod =
              io.grpc.MethodDescriptor.<proto.ResourceService.ResourceCommandRequest, proto.ResourceService.ResourceCommandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ExecuteResourceCommand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.ResourceService.ResourceCommandRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.ResourceService.ResourceCommandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DashboardServiceMethodDescriptorSupplier("ExecuteResourceCommand"))
              .build();
        }
      }
    }
    return getExecuteResourceCommandMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DashboardServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DashboardServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DashboardServiceStub>() {
        @java.lang.Override
        public DashboardServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DashboardServiceStub(channel, callOptions);
        }
      };
    return DashboardServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DashboardServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DashboardServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DashboardServiceBlockingStub>() {
        @java.lang.Override
        public DashboardServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DashboardServiceBlockingStub(channel, callOptions);
        }
      };
    return DashboardServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DashboardServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DashboardServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DashboardServiceFutureStub>() {
        @java.lang.Override
        public DashboardServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DashboardServiceFutureStub(channel, callOptions);
        }
      };
    return DashboardServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getApplicationInformation(proto.ResourceService.ApplicationInformationRequest request,
        io.grpc.stub.StreamObserver<proto.ResourceService.ApplicationInformationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetApplicationInformationMethod(), responseObserver);
    }

    /**
     */
    default void watchResources(proto.ResourceService.WatchResourcesRequest request,
        io.grpc.stub.StreamObserver<proto.ResourceService.WatchResourcesUpdate> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWatchResourcesMethod(), responseObserver);
    }

    /**
     */
    default void watchResourceConsoleLogs(proto.ResourceService.WatchResourceConsoleLogsRequest request,
        io.grpc.stub.StreamObserver<proto.ResourceService.WatchResourceConsoleLogsUpdate> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWatchResourceConsoleLogsMethod(), responseObserver);
    }

    /**
     */
    default void executeResourceCommand(proto.ResourceService.ResourceCommandRequest request,
        io.grpc.stub.StreamObserver<proto.ResourceService.ResourceCommandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExecuteResourceCommandMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DashboardService.
   */
  public static abstract class DashboardServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DashboardServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DashboardService.
   */
  public static final class DashboardServiceStub
      extends io.grpc.stub.AbstractAsyncStub<DashboardServiceStub> {
    private DashboardServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DashboardServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DashboardServiceStub(channel, callOptions);
    }

    /**
     */
    public void getApplicationInformation(proto.ResourceService.ApplicationInformationRequest request,
        io.grpc.stub.StreamObserver<proto.ResourceService.ApplicationInformationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetApplicationInformationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void watchResources(proto.ResourceService.WatchResourcesRequest request,
        io.grpc.stub.StreamObserver<proto.ResourceService.WatchResourcesUpdate> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getWatchResourcesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void watchResourceConsoleLogs(proto.ResourceService.WatchResourceConsoleLogsRequest request,
        io.grpc.stub.StreamObserver<proto.ResourceService.WatchResourceConsoleLogsUpdate> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getWatchResourceConsoleLogsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void executeResourceCommand(proto.ResourceService.ResourceCommandRequest request,
        io.grpc.stub.StreamObserver<proto.ResourceService.ResourceCommandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExecuteResourceCommandMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DashboardService.
   */
  public static final class DashboardServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DashboardServiceBlockingStub> {
    private DashboardServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DashboardServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DashboardServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public proto.ResourceService.ApplicationInformationResponse getApplicationInformation(proto.ResourceService.ApplicationInformationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetApplicationInformationMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<proto.ResourceService.WatchResourcesUpdate> watchResources(
        proto.ResourceService.WatchResourcesRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getWatchResourcesMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<proto.ResourceService.WatchResourceConsoleLogsUpdate> watchResourceConsoleLogs(
        proto.ResourceService.WatchResourceConsoleLogsRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getWatchResourceConsoleLogsMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.ResourceService.ResourceCommandResponse executeResourceCommand(proto.ResourceService.ResourceCommandRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExecuteResourceCommandMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DashboardService.
   */
  public static final class DashboardServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<DashboardServiceFutureStub> {
    private DashboardServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DashboardServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DashboardServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.ResourceService.ApplicationInformationResponse> getApplicationInformation(
        proto.ResourceService.ApplicationInformationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetApplicationInformationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.ResourceService.ResourceCommandResponse> executeResourceCommand(
        proto.ResourceService.ResourceCommandRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExecuteResourceCommandMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_APPLICATION_INFORMATION = 0;
  private static final int METHODID_WATCH_RESOURCES = 1;
  private static final int METHODID_WATCH_RESOURCE_CONSOLE_LOGS = 2;
  private static final int METHODID_EXECUTE_RESOURCE_COMMAND = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_APPLICATION_INFORMATION:
          serviceImpl.getApplicationInformation((proto.ResourceService.ApplicationInformationRequest) request,
              (io.grpc.stub.StreamObserver<proto.ResourceService.ApplicationInformationResponse>) responseObserver);
          break;
        case METHODID_WATCH_RESOURCES:
          serviceImpl.watchResources((proto.ResourceService.WatchResourcesRequest) request,
              (io.grpc.stub.StreamObserver<proto.ResourceService.WatchResourcesUpdate>) responseObserver);
          break;
        case METHODID_WATCH_RESOURCE_CONSOLE_LOGS:
          serviceImpl.watchResourceConsoleLogs((proto.ResourceService.WatchResourceConsoleLogsRequest) request,
              (io.grpc.stub.StreamObserver<proto.ResourceService.WatchResourceConsoleLogsUpdate>) responseObserver);
          break;
        case METHODID_EXECUTE_RESOURCE_COMMAND:
          serviceImpl.executeResourceCommand((proto.ResourceService.ResourceCommandRequest) request,
              (io.grpc.stub.StreamObserver<proto.ResourceService.ResourceCommandResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetApplicationInformationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.ResourceService.ApplicationInformationRequest,
              proto.ResourceService.ApplicationInformationResponse>(
                service, METHODID_GET_APPLICATION_INFORMATION)))
        .addMethod(
          getWatchResourcesMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              proto.ResourceService.WatchResourcesRequest,
              proto.ResourceService.WatchResourcesUpdate>(
                service, METHODID_WATCH_RESOURCES)))
        .addMethod(
          getWatchResourceConsoleLogsMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              proto.ResourceService.WatchResourceConsoleLogsRequest,
              proto.ResourceService.WatchResourceConsoleLogsUpdate>(
                service, METHODID_WATCH_RESOURCE_CONSOLE_LOGS)))
        .addMethod(
          getExecuteResourceCommandMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.ResourceService.ResourceCommandRequest,
              proto.ResourceService.ResourceCommandResponse>(
                service, METHODID_EXECUTE_RESOURCE_COMMAND)))
        .build();
  }

  private static abstract class DashboardServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DashboardServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto.ResourceService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DashboardService");
    }
  }

  private static final class DashboardServiceFileDescriptorSupplier
      extends DashboardServiceBaseDescriptorSupplier {
    DashboardServiceFileDescriptorSupplier() {}
  }

  private static final class DashboardServiceMethodDescriptorSupplier
      extends DashboardServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DashboardServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DashboardServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DashboardServiceFileDescriptorSupplier())
              .addMethod(getGetApplicationInformationMethod())
              .addMethod(getWatchResourcesMethod())
              .addMethod(getWatchResourceConsoleLogsMethod())
              .addMethod(getExecuteResourceCommandMethod())
              .build();
        }
      }
    }
    return result;
  }
}
