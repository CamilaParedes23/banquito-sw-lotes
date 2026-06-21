package com.banquito.switchpagos.batch.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.65.1)",
    comments = "Source: batch_gateway.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BatchGatewayServiceGrpc {

  private BatchGatewayServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "banquito.switchpagos.batch.v1.BatchGatewayService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest,
      com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse> getUploadBatchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UploadBatch",
      requestType = com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest.class,
      responseType = com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest,
      com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse> getUploadBatchMethod() {
    io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest, com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse> getUploadBatchMethod;
    if ((getUploadBatchMethod = BatchGatewayServiceGrpc.getUploadBatchMethod) == null) {
      synchronized (BatchGatewayServiceGrpc.class) {
        if ((getUploadBatchMethod = BatchGatewayServiceGrpc.getUploadBatchMethod) == null) {
          BatchGatewayServiceGrpc.getUploadBatchMethod = getUploadBatchMethod =
              io.grpc.MethodDescriptor.<com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest, com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UploadBatch"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BatchGatewayServiceMethodDescriptorSupplier("UploadBatch"))
              .build();
        }
      }
    }
    return getUploadBatchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest,
      com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse> getGetBatchStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBatchStatus",
      requestType = com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest.class,
      responseType = com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest,
      com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse> getGetBatchStatusMethod() {
    io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest, com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse> getGetBatchStatusMethod;
    if ((getGetBatchStatusMethod = BatchGatewayServiceGrpc.getGetBatchStatusMethod) == null) {
      synchronized (BatchGatewayServiceGrpc.class) {
        if ((getGetBatchStatusMethod = BatchGatewayServiceGrpc.getGetBatchStatusMethod) == null) {
          BatchGatewayServiceGrpc.getGetBatchStatusMethod = getGetBatchStatusMethod =
              io.grpc.MethodDescriptor.<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest, com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBatchStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BatchGatewayServiceMethodDescriptorSupplier("GetBatchStatus"))
              .build();
        }
      }
    }
    return getGetBatchStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest,
      com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse> getGetBatchValidationErrorsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBatchValidationErrors",
      requestType = com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest.class,
      responseType = com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest,
      com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse> getGetBatchValidationErrorsMethod() {
    io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest, com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse> getGetBatchValidationErrorsMethod;
    if ((getGetBatchValidationErrorsMethod = BatchGatewayServiceGrpc.getGetBatchValidationErrorsMethod) == null) {
      synchronized (BatchGatewayServiceGrpc.class) {
        if ((getGetBatchValidationErrorsMethod = BatchGatewayServiceGrpc.getGetBatchValidationErrorsMethod) == null) {
          BatchGatewayServiceGrpc.getGetBatchValidationErrorsMethod = getGetBatchValidationErrorsMethod =
              io.grpc.MethodDescriptor.<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest, com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBatchValidationErrors"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BatchGatewayServiceMethodDescriptorSupplier("GetBatchValidationErrors"))
              .build();
        }
      }
    }
    return getGetBatchValidationErrorsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest,
      com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse> getGetBatchLinesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBatchLines",
      requestType = com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest.class,
      responseType = com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest,
      com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse> getGetBatchLinesMethod() {
    io.grpc.MethodDescriptor<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest, com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse> getGetBatchLinesMethod;
    if ((getGetBatchLinesMethod = BatchGatewayServiceGrpc.getGetBatchLinesMethod) == null) {
      synchronized (BatchGatewayServiceGrpc.class) {
        if ((getGetBatchLinesMethod = BatchGatewayServiceGrpc.getGetBatchLinesMethod) == null) {
          BatchGatewayServiceGrpc.getGetBatchLinesMethod = getGetBatchLinesMethod =
              io.grpc.MethodDescriptor.<com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest, com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBatchLines"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BatchGatewayServiceMethodDescriptorSupplier("GetBatchLines"))
              .build();
        }
      }
    }
    return getGetBatchLinesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BatchGatewayServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BatchGatewayServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BatchGatewayServiceStub>() {
        @java.lang.Override
        public BatchGatewayServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BatchGatewayServiceStub(channel, callOptions);
        }
      };
    return BatchGatewayServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BatchGatewayServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BatchGatewayServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BatchGatewayServiceBlockingStub>() {
        @java.lang.Override
        public BatchGatewayServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BatchGatewayServiceBlockingStub(channel, callOptions);
        }
      };
    return BatchGatewayServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BatchGatewayServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BatchGatewayServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BatchGatewayServiceFutureStub>() {
        @java.lang.Override
        public BatchGatewayServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BatchGatewayServiceFutureStub(channel, callOptions);
        }
      };
    return BatchGatewayServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void uploadBatch(com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUploadBatchMethod(), responseObserver);
    }

    /**
     */
    default void getBatchStatus(com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBatchStatusMethod(), responseObserver);
    }

    /**
     */
    default void getBatchValidationErrors(com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBatchValidationErrorsMethod(), responseObserver);
    }

    /**
     */
    default void getBatchLines(com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBatchLinesMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BatchGatewayService.
   */
  public static abstract class BatchGatewayServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BatchGatewayServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BatchGatewayService.
   */
  public static final class BatchGatewayServiceStub
      extends io.grpc.stub.AbstractAsyncStub<BatchGatewayServiceStub> {
    private BatchGatewayServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BatchGatewayServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BatchGatewayServiceStub(channel, callOptions);
    }

    /**
     */
    public void uploadBatch(com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUploadBatchMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBatchStatus(com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBatchStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBatchValidationErrors(com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBatchValidationErrorsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBatchLines(com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBatchLinesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BatchGatewayService.
   */
  public static final class BatchGatewayServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BatchGatewayServiceBlockingStub> {
    private BatchGatewayServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BatchGatewayServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BatchGatewayServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse uploadBatch(com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUploadBatchMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse getBatchStatus(com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBatchStatusMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse getBatchValidationErrors(com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBatchValidationErrorsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse getBatchLines(com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBatchLinesMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BatchGatewayService.
   */
  public static final class BatchGatewayServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<BatchGatewayServiceFutureStub> {
    private BatchGatewayServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BatchGatewayServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BatchGatewayServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse> uploadBatch(
        com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUploadBatchMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse> getBatchStatus(
        com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBatchStatusMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse> getBatchValidationErrors(
        com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBatchValidationErrorsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse> getBatchLines(
        com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBatchLinesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_UPLOAD_BATCH = 0;
  private static final int METHODID_GET_BATCH_STATUS = 1;
  private static final int METHODID_GET_BATCH_VALIDATION_ERRORS = 2;
  private static final int METHODID_GET_BATCH_LINES = 3;

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
        case METHODID_UPLOAD_BATCH:
          serviceImpl.uploadBatch((com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse>) responseObserver);
          break;
        case METHODID_GET_BATCH_STATUS:
          serviceImpl.getBatchStatus((com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse>) responseObserver);
          break;
        case METHODID_GET_BATCH_VALIDATION_ERRORS:
          serviceImpl.getBatchValidationErrors((com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse>) responseObserver);
          break;
        case METHODID_GET_BATCH_LINES:
          serviceImpl.getBatchLines((com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse>) responseObserver);
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
          getUploadBatchMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.banquito.switchpagos.batch.grpc.UploadBatchGrpcRequest,
              com.banquito.switchpagos.batch.grpc.UploadBatchGrpcResponse>(
                service, METHODID_UPLOAD_BATCH)))
        .addMethod(
          getGetBatchStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest,
              com.banquito.switchpagos.batch.grpc.BatchStatusGrpcResponse>(
                service, METHODID_GET_BATCH_STATUS)))
        .addMethod(
          getGetBatchValidationErrorsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest,
              com.banquito.switchpagos.batch.grpc.BatchValidationErrorsGrpcResponse>(
                service, METHODID_GET_BATCH_VALIDATION_ERRORS)))
        .addMethod(
          getGetBatchLinesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.banquito.switchpagos.batch.grpc.BatchIdGrpcRequest,
              com.banquito.switchpagos.batch.grpc.BatchLinesGrpcResponse>(
                service, METHODID_GET_BATCH_LINES)))
        .build();
  }

  private static abstract class BatchGatewayServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BatchGatewayServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.banquito.switchpagos.batch.grpc.BatchGatewayProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BatchGatewayService");
    }
  }

  private static final class BatchGatewayServiceFileDescriptorSupplier
      extends BatchGatewayServiceBaseDescriptorSupplier {
    BatchGatewayServiceFileDescriptorSupplier() {}
  }

  private static final class BatchGatewayServiceMethodDescriptorSupplier
      extends BatchGatewayServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    BatchGatewayServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (BatchGatewayServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BatchGatewayServiceFileDescriptorSupplier())
              .addMethod(getUploadBatchMethod())
              .addMethod(getGetBatchStatusMethod())
              .addMethod(getGetBatchValidationErrorsMethod())
              .addMethod(getGetBatchLinesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
