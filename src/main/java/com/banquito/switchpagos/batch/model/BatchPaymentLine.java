package com.banquito.switchpagos.batch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"LINEA_PAGO_LOTE\"")
public class BatchPaymentLine {

    @Id
    @Column(name = "\"ID_LINEA\"")
    private UUID lineId;

    @Column(name = "\"ID_LOTE\"", nullable = false)
    private UUID batchId;

    @Column(name = "\"NUMERO_SECUENCIA\"", nullable = false)
    private Integer sequenceNumber;

    @Column(name = "\"IDENTIFICACION_BENEFICIARIO\"", nullable = false)
    private String beneficiaryIdentification;

    @Column(name = "\"NOMBRE_BENEFICIARIO\"", nullable = false)
    private String beneficiaryName;

    @Column(name = "\"NUMERO_CUENTA_DESTINO\"", nullable = false)
    private String destinationAccountNumber;

    @Column(name = "\"CODIGO_ENRUTAMIENTO\"", nullable = false)
    private String routingCode;

    @Column(name = "\"MONTO\"", nullable = false)
    private BigDecimal amount;

    @Column(name = "\"MONEDA\"", nullable = false)
    private String currency;

    @Column(name = "\"REFERENCIA\"")
    private String reference;

    @Column(name = "\"EMAIL_NOTIFICACION\"")
    private String notificationEmail;

    @Column(name = "\"ESTADO\"", nullable = false)
    private String status;

    @Column(name = "\"ID_EVENTO\"")
    private UUID eventId;

    @Column(name = "\"FECHA_PUBLICACION\"")
    private OffsetDateTime publishedAt;

    @Column(name = "\"FECHA_CREACION\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"FECHA_ACTUALIZACION\"", nullable = false)
    private OffsetDateTime updatedAt;

    public BatchPaymentLine() {
    }

    public BatchPaymentLine(UUID lineId) {
        this.lineId = lineId;
    }

    public UUID getLineId() {
        return lineId;
    }

    public void setLineId(UUID lineId) {
        this.lineId = lineId;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
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
        if (!(object instanceof BatchPaymentLine that)) {
            return false;
        }
        return Objects.equals(lineId, that.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId);
    }

    @Override
    public String toString() {
        return "BatchPaymentLine{lineId=" + lineId + ", batchId=" + batchId + ", status='" + status + "'}";
    }
}
