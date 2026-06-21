package com.banquito.switchpagos.batch.mapper;

import com.banquito.switchpagos.batch.dto.response.BatchStatusResponse;
import com.banquito.switchpagos.batch.dto.response.UploadBatchResponse;
import com.banquito.switchpagos.batch.model.PaymentBatch;
import org.springframework.stereotype.Component;

@Component
public class BatchMapper {

    public UploadBatchResponse toUploadResponse(PaymentBatch batch) {
        UploadBatchResponse response = new UploadBatchResponse();
        response.setBatchId(batch.getBatchId());
        response.setStatus(batch.getStatus());
        response.setReceivedAt(batch.getReceivedAt());
        response.setCorrelationId(batch.getCorrelationId());
        response.setMessage("Lote recibido. La validacion profunda y el fondeo se ejecutan de forma asincrona.");
        return response;
    }

    public BatchStatusResponse toStatusResponse(PaymentBatch batch) {
        BatchStatusResponse response = new BatchStatusResponse();
        response.setBatchId(batch.getBatchId());
        response.setStatus(batch.getStatus());
        response.setCompanyRuc(batch.getCompanyRuc());
        response.setSourceAccountNumber(batch.getSourceAccountNumber());
        response.setServiceType(batch.getServiceType());
        response.setFileName(batch.getFileName());
        response.setTotalRecords(batch.getTotalRecords());
        response.setControlAmount(batch.getControlAmount());
        response.setReceivedAt(batch.getReceivedAt());
        response.setValidatedAt(batch.getValidatedAt());
        response.setFundedAt(batch.getFundedAt());
        response.setCoreFundingId(batch.getCoreFundingId());
        response.setCoreTransactionId(batch.getCoreTransactionId());
        response.setAccountingDate(batch.getAccountingDate());
        response.setMessage(batch.getRejectionReason());
        return response;
    }
}
