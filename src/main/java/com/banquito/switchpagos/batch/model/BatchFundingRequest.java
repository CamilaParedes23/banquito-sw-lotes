package com.banquito.switchpagos.batch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"SOLICITUD_FONDEO_LOTE\"")
public class BatchFundingRequest {

    @Id
    @Column(name = "\"ID_SOLICITUD_FONDEO\"")
    private UUID fundingRequestId;

    @Column(name = "\"ID_LOTE\"", nullable = false)
    private UUID batchId;

    @Column(name = "\"CLAVE_IDEMPOTENCIA\"", nullable = false)
    private String idempotencyKey;

    @Column(name = "\"ESTADO_SOLICITUD\"", nullable = false)
    private String requestStatus;

    @Column(name = "\"MONTO_SOLICITADO\"", nullable = false)
    private BigDecimal requestedAmount;

    @Column(name = "\"MONEDA\"", nullable = false)
    private String currency;

    @Column(name = "\"ID_FONDEO_CORE\"")
    private String coreFundingId;

    @Column(name = "\"ID_TRANSACCION_CORE\"")
    private String coreTransactionId;

    @Column(name = "\"FECHA_CONTABLE\"")
    private LocalDate accountingDate;

    @Column(name = "\"ESTADO_RESPUESTA_CORE\"")
    private String coreResponseStatus;

    @Column(name = "\"MENSAJE_RESPUESTA_CORE\"")
    private String coreResponseMessage;

    @Column(name = "\"FECHA_SOLICITUD\"", nullable = false)
    private OffsetDateTime requestedAt;

    @Column(name = "\"FECHA_RESPUESTA\"")
    private OffsetDateTime respondedAt;

    public BatchFundingRequest() {
    }

    public BatchFundingRequest(UUID fundingRequestId) {
        this.fundingRequestId = fundingRequestId;
    }

    public UUID getFundingRequestId() {
        return fundingRequestId;
    }

    public void setFundingRequestId(UUID fundingRequestId) {
        this.fundingRequestId = fundingRequestId;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCoreFundingId() {
        return coreFundingId;
    }

    public void setCoreFundingId(String coreFundingId) {
        this.coreFundingId = coreFundingId;
    }

    public String getCoreTransactionId() {
        return coreTransactionId;
    }

    public void setCoreTransactionId(String coreTransactionId) {
        this.coreTransactionId = coreTransactionId;
    }

    public LocalDate getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(LocalDate accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getCoreResponseStatus() {
        return coreResponseStatus;
    }

    public void setCoreResponseStatus(String coreResponseStatus) {
        this.coreResponseStatus = coreResponseStatus;
    }

    public String getCoreResponseMessage() {
        return coreResponseMessage;
    }

    public void setCoreResponseMessage(String coreResponseMessage) {
        this.coreResponseMessage = coreResponseMessage;
    }

    public OffsetDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(OffsetDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public OffsetDateTime getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(OffsetDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BatchFundingRequest that)) {
            return false;
        }
        return Objects.equals(fundingRequestId, that.fundingRequestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fundingRequestId);
    }

    @Override
    public String toString() {
        return "BatchFundingRequest{fundingRequestId=" + fundingRequestId + ", status='" + requestStatus + "'}";
    }
}
