package com.banquito.switchpagos.batch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"ERROR_VALIDACION_LOTE\"")
public class BatchValidationError {

    @Id
    @Column(name = "\"ID_ERROR_VALIDACION\"")
    private UUID validationErrorId;

    @Column(name = "\"ID_LOTE\"", nullable = false)
    private UUID batchId;

    @Column(name = "\"CODIGO\"", nullable = false)
    private String code;

    @Column(name = "\"NOMBRE_CAMPO\"")
    private String fieldName;

    @Column(name = "\"MENSAJE\"", nullable = false)
    private String message;

    @Column(name = "\"FECHA_CREACION\"", nullable = false)
    private OffsetDateTime createdAt;

    public BatchValidationError() {
    }

    public BatchValidationError(UUID validationErrorId) {
        this.validationErrorId = validationErrorId;
    }

    public UUID getValidationErrorId() {
        return validationErrorId;
    }

    public void setValidationErrorId(UUID validationErrorId) {
        this.validationErrorId = validationErrorId;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BatchValidationError that)) {
            return false;
        }
        return Objects.equals(validationErrorId, that.validationErrorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validationErrorId);
    }

    @Override
    public String toString() {
        return "BatchValidationError{validationErrorId=" + validationErrorId + ", code='" + code + "'}";
    }
}
