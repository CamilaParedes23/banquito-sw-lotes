package com.banquito.switchpagos.batch.grpc;

import com.banquito.switchpagos.batch.dto.response.BatchLineResponse;
import com.banquito.switchpagos.batch.dto.response.BatchLinesResponse;
import com.banquito.switchpagos.batch.dto.response.BatchStatusResponse;
import com.banquito.switchpagos.batch.dto.response.BatchValidationErrorsResponse;
import com.banquito.switchpagos.batch.dto.response.UploadBatchResponse;
import com.banquito.switchpagos.batch.dto.response.ValidationErrorResponse;
import com.banquito.switchpagos.batch.exception.BadRequestException;
import com.banquito.switchpagos.batch.exception.ResourceNotFoundException;
import com.banquito.switchpagos.batch.service.BatchService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class GrpcBatchGatewayService extends BatchGatewayServiceGrpc.BatchGatewayServiceImplBase {

    private final BatchService batchService;

    public GrpcBatchGatewayService(BatchService batchService) {
        this.batchService = batchService;
    }

    @Override
    public void uploadBatch(UploadBatchGrpcRequest request, StreamObserver<UploadBatchGrpcResponse> responseObserver) {
        try {
            GrpcMultipartFile file = new GrpcMultipartFile(
                    "file",
                    request.getFileName(),
                    request.getContentType(),
                    request.getContent().toByteArray());
            UploadBatchResponse response = batchService.uploadBatch(
                    file,
                    null,
                    null,
                    request.getChannel(),
                    request.getReceivedBy());
            responseObserver.onNext(toGrpc(response));
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(toStatus(exception));
        }
    }

    @Override
    public void getBatchStatus(BatchIdGrpcRequest request, StreamObserver<BatchStatusGrpcResponse> responseObserver) {
        try {
            responseObserver.onNext(toGrpc(batchService.getBatch(parseBatchId(request.getBatchId()))));
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(toStatus(exception));
        }
    }

    @Override
    public void getBatchValidationErrors(
            BatchIdGrpcRequest request,
            StreamObserver<BatchValidationErrorsGrpcResponse> responseObserver) {
        try {
            responseObserver.onNext(toGrpc(batchService.getValidationErrors(parseBatchId(request.getBatchId()))));
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(toStatus(exception));
        }
    }

    @Override
    public void getBatchLines(BatchIdGrpcRequest request, StreamObserver<BatchLinesGrpcResponse> responseObserver) {
        try {
            responseObserver.onNext(toGrpc(batchService.getLines(parseBatchId(request.getBatchId()))));
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(toStatus(exception));
        }
    }

    private UUID parseBatchId(String batchId) {
        try {
            return UUID.fromString(batchId);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("El batchId no tiene formato UUID valido.", exception);
        }
    }

    private UploadBatchGrpcResponse toGrpc(UploadBatchResponse response) {
        return UploadBatchGrpcResponse.newBuilder()
                .setBatchId(toString(response.getBatchId()))
                .setStatus(nullToEmpty(response.getStatus()))
                .setMessage(nullToEmpty(response.getMessage()))
                .setReceivedAt(toString(response.getReceivedAt()))
                .setCorrelationId(toString(response.getCorrelationId()))
                .build();
    }

    private BatchStatusGrpcResponse toGrpc(BatchStatusResponse response) {
        return BatchStatusGrpcResponse.newBuilder()
                .setBatchId(toString(response.getBatchId()))
                .setStatus(nullToEmpty(response.getStatus()))
                .setCompanyRuc(nullToEmpty(response.getCompanyRuc()))
                .setSourceAccountNumber(nullToEmpty(response.getSourceAccountNumber()))
                .setServiceType(nullToEmpty(response.getServiceType()))
                .setFileName(nullToEmpty(response.getFileName()))
                .setTotalRecords(toInt(response.getTotalRecords()))
                .setControlAmount(toString(response.getControlAmount()))
                .setReceivedAt(toString(response.getReceivedAt()))
                .setValidatedAt(toString(response.getValidatedAt()))
                .setFundedAt(toString(response.getFundedAt()))
                .setCoreFundingId(nullToEmpty(response.getCoreFundingId()))
                .setCoreTransactionId(nullToEmpty(response.getCoreTransactionId()))
                .setAccountingDate(toString(response.getAccountingDate()))
                .setMessage(nullToEmpty(response.getMessage()))
                .build();
    }

    private BatchValidationErrorsGrpcResponse toGrpc(BatchValidationErrorsResponse response) {
        BatchValidationErrorsGrpcResponse.Builder builder = BatchValidationErrorsGrpcResponse.newBuilder()
                .setBatchId(toString(response.getBatchId()));
        for (ValidationErrorResponse error : response.getErrors()) {
            builder.addErrors(ValidationErrorGrpc.newBuilder()
                    .setCode(nullToEmpty(error.getCode()))
                    .setMessage(nullToEmpty(error.getMessage()))
                    .setField(nullToEmpty(error.getField()))
                    .build());
        }
        return builder.build();
    }

    private BatchLinesGrpcResponse toGrpc(BatchLinesResponse response) {
        BatchLinesGrpcResponse.Builder builder = BatchLinesGrpcResponse.newBuilder()
                .setBatchId(toString(response.getBatchId()));
        for (BatchLineResponse line : response.getLines()) {
            builder.addLines(BatchLineGrpc.newBuilder()
                    .setLineId(toString(line.getLineId()))
                    .setSequenceNumber(toInt(line.getSequenceNumber()))
                    .setBeneficiaryIdentification(nullToEmpty(line.getBeneficiaryIdentification()))
                    .setBeneficiaryName(nullToEmpty(line.getBeneficiaryName()))
                    .setDestinationAccountNumber(nullToEmpty(line.getDestinationAccountNumber()))
                    .setRoutingCode(nullToEmpty(line.getRoutingCode()))
                    .setAmount(toString(line.getAmount()))
                    .setStatus(nullToEmpty(line.getStatus()))
                    .setEventId(toString(line.getEventId()))
                    .build());
        }
        return builder.build();
    }

    private Throwable toStatus(Exception exception) {
        if (exception instanceof BadRequestException) {
            return Status.INVALID_ARGUMENT.withDescription(exception.getMessage()).asRuntimeException();
        }
        if (exception instanceof ResourceNotFoundException) {
            return Status.NOT_FOUND.withDescription(exception.getMessage()).asRuntimeException();
        }
        return Status.INTERNAL.withDescription("Error tecnico en batch-service.").asRuntimeException();
    }

    private String toString(UUID value) {
        return value == null ? "" : value.toString();
    }

    private String toString(OffsetDateTime value) {
        return value == null ? "" : value.toString();
    }

    private String toString(LocalDate value) {
        return value == null ? "" : value.toString();
    }

    private String toString(BigDecimal value) {
        return value == null ? "" : value.toPlainString();
    }

    private Integer toInt(Integer value) {
        return value == null ? 0 : value;
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
