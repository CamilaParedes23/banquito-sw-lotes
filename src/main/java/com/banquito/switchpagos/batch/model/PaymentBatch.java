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
@Table(name = "\"LOTE_PAGO_MASIVO\"")
public class PaymentBatch {

    @Id
    @Column(name = "\"ID_LOTE\"")
    private UUID batchId;

    @Column(name = "\"ID_CORRELACION\"", nullable = false)
    private UUID correlationId;

    @Column(name = "\"RUC_EMPRESA\"", nullable = false)
    private String companyRuc;

    @Column(name = "\"NUMERO_CUENTA_MATRIZ\"", nullable = false)
    private String sourceAccountNumber;

    @Column(name = "\"TIPO_SERVICIO\"", nullable = false)
    private String serviceType;

    @Column(name = "\"NOMBRE_ARCHIVO\"", nullable = false)
    private String fileName;

    @Column(name = "\"HASH_ARCHIVO\"", nullable = false)
    private String fileHash;

    @Column(name = "\"CANAL\"", nullable = false)
    private String channel;

    @Column(name = "\"RECIBIDO_POR\"")
    private String receivedBy;

    @Column(name = "\"TOTAL_REGISTROS\"", nullable = false)
    private Integer totalRecords;

    @Column(name = "\"MONTO_CONTROL\"", nullable = false)
    private BigDecimal controlAmount;

    @Column(name = "\"MONEDA\"", nullable = false)
    private String currency;

    @Column(name = "\"ESTADO\"", nullable = false)
    private String status;

    @Column(name = "\"MOTIVO_RECHAZO\"")
    private String rejectionReason;

    @Column(name = "\"FECHA_RECEPCION\"", nullable = false)
    private OffsetDateTime receivedAt;

    @Column(name = "\"FECHA_VALIDACION\"")
    private OffsetDateTime validatedAt;

    @Column(name = "\"FECHA_FONDEO\"")
    private OffsetDateTime fundedAt;

    @Column(name = "\"ID_FONDEO_CORE\"")
    private String coreFundingId;

    @Column(name = "\"ID_TRANSACCION_CORE\"")
    private String coreTransactionId;

    @Column(name = "\"FECHA_CONTABLE\"")
    private LocalDate accountingDate;

    @Column(name = "\"FECHA_CREACION\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"FECHA_ACTUALIZACION\"", nullable = false)
    private OffsetDateTime updatedAt;

    public PaymentBatch() {
    }

    public PaymentBatch(UUID batchId) {
        this.batchId = batchId;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public BigDecimal getControlAmount() {
        return controlAmount;
    }

    public void setControlAmount(BigDecimal controlAmount) {
        this.controlAmount = controlAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public OffsetDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(OffsetDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public OffsetDateTime getValidatedAt() {
        return validatedAt;
    }

    public void setValidatedAt(OffsetDateTime validatedAt) {
        this.validatedAt = validatedAt;
    }

    public OffsetDateTime getFundedAt() {
        return fundedAt;
    }

    public void setFundedAt(OffsetDateTime fundedAt) {
        this.fundedAt = fundedAt;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PaymentBatch that)) {
            return false;
        }
        return Objects.equals(batchId, that.batchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(batchId);
    }

    @Override
    public String toString() {
        return "PaymentBatch{batchId=" + batchId + ", status='" + status + "'}";
    }
}
