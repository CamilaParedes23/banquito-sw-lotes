package com.banquito.switchpagos.batch.mapper;

import com.banquito.switchpagos.batch.dto.event.PaymentLineRequestedEvent;
import com.banquito.switchpagos.batch.dto.request.ParsedPaymentLine;
import com.banquito.switchpagos.batch.dto.response.BatchLineResponse;
import com.banquito.switchpagos.batch.enums.LineStatus;
import com.banquito.switchpagos.batch.model.BatchPaymentLine;
import com.banquito.switchpagos.batch.model.PaymentBatch;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PaymentLineMapper {

    public BatchPaymentLine toEntity(UUID batchId, ParsedPaymentLine parsedLine) {
        OffsetDateTime now = OffsetDateTime.now();
        BatchPaymentLine line = new BatchPaymentLine();
        line.setLineId(UUID.randomUUID());
        line.setBatchId(batchId);
        line.setSequenceNumber(parsedLine.getSequenceNumber());
        line.setBeneficiaryIdentification(parsedLine.getBeneficiaryIdentification());
        line.setBeneficiaryName(parsedLine.getBeneficiaryName());
        line.setDestinationAccountNumber(parsedLine.getDestinationAccountNumber());
        line.setRoutingCode(parsedLine.getRoutingCode());
        line.setAmount(parsedLine.getAmount());
        line.setCurrency("USD");
        line.setReference(parsedLine.getReference());
        line.setNotificationEmail(parsedLine.getNotificationEmail());
        line.setStatus(LineStatus.PENDIENTE.name());
        line.setCreatedAt(now);
        line.setUpdatedAt(now);
        return line;
    }

    public PaymentLineRequestedEvent toEvent(PaymentBatch batch, BatchPaymentLine line, UUID eventId) {
        PaymentLineRequestedEvent event = new PaymentLineRequestedEvent();
        event.setEventId(eventId);
        event.setEventType("PAYMENT_LINE_REQUESTED");
        event.setOccurredAt(OffsetDateTime.now());
        event.setBatchId(batch.getBatchId());
        event.setLineId(line.getLineId());
        event.setCorrelationId(batch.getCorrelationId());
        event.setSourceService("banquito-switch-batch-service");
        event.setCompanyRuc(batch.getCompanyRuc());
        event.setSourceAccountNumber(batch.getSourceAccountNumber());
        event.setCoreFundingId(batch.getCoreFundingId());
        event.setBatchTotalLines(batch.getTotalRecords());
        event.setBatchControlAmount(batch.getControlAmount());
        event.setSequenceNumber(line.getSequenceNumber());
        event.setBeneficiaryIdentification(line.getBeneficiaryIdentification());
        event.setBeneficiaryName(line.getBeneficiaryName());
        event.setDestinationAccountNumber(line.getDestinationAccountNumber());
        event.setRoutingCode(line.getRoutingCode());
        event.setAmount(line.getAmount());
        event.setCurrency(batch.getCurrency());
        event.setReference(line.getReference());
        event.setNotificationEmail(line.getNotificationEmail());
        return event;
    }

    public BatchLineResponse toResponse(BatchPaymentLine line) {
        BatchLineResponse response = new BatchLineResponse();
        response.setLineId(line.getLineId());
        response.setSequenceNumber(line.getSequenceNumber());
        response.setBeneficiaryIdentification(line.getBeneficiaryIdentification());
        response.setBeneficiaryName(line.getBeneficiaryName());
        response.setDestinationAccountNumber(line.getDestinationAccountNumber());
        response.setRoutingCode(line.getRoutingCode());
        response.setAmount(line.getAmount());
        response.setStatus(line.getStatus());
        response.setEventId(line.getEventId());
        return response;
    }
}
