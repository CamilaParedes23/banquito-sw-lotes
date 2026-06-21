package com.banquito.switchpagos.batch.dto.event;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PaymentLineRequestedEvent {

    private UUID eventId;
    private String eventType;
    private OffsetDateTime occurredAt;
    private UUID batchId;
    private UUID lineId;
    private UUID correlationId;
    private String sourceService;
    private Integer sequenceNumber;
    private String companyRuc;
    private String sourceAccountNumber;
    private String coreFundingId;
    private Integer batchTotalLines;
    private BigDecimal batchControlAmount;
    private String beneficiaryIdentification;
    private String beneficiaryName;
    private String destinationAccountNumber;
    private String routingCode;
    private BigDecimal amount;
    private String currency;
    private String reference;
    private String notificationEmail;

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(OffsetDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public UUID getLineId() {
        return lineId;
    }

    public void setLineId(UUID lineId) {
        this.lineId = lineId;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public String getSourceService() {
        return sourceService;
    }

    public void setSourceService(String sourceService) {
        this.sourceService = sourceService;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getCompanyRuc() {
        return companyRuc;
    }

    public void setCompanyRuc(String companyRuc) {
        this.companyRuc = companyRuc;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getCoreFundingId() {
        return coreFundingId;
    }

    public void setCoreFundingId(String coreFundingId) {
        this.coreFundingId = coreFundingId;
    }

    public Integer getBatchTotalLines() {
        return batchTotalLines;
    }

    public void setBatchTotalLines(Integer batchTotalLines) {
        this.batchTotalLines = batchTotalLines;
    }

    public BigDecimal getBatchControlAmount() {
        return batchControlAmount;
    }

    public void setBatchControlAmount(BigDecimal batchControlAmount) {
        this.batchControlAmount = batchControlAmount;
    }

    public String getBeneficiaryIdentification() {
        return beneficiaryIdentification;
    }

    public void setBeneficiaryIdentification(String beneficiaryIdentification) {
        this.beneficiaryIdentification = beneficiaryIdentification;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getRoutingCode() {
        return routingCode;
    }

    public void setRoutingCode(String routingCode) {
        this.routingCode = routingCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }
}
