package com.banquito.switchpagos.batch.dto.response;

import java.util.List;
import java.util.UUID;

public class BatchValidationErrorsResponse {

    private UUID batchId;
    private List<ValidationErrorResponse> errors;

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public List<ValidationErrorResponse> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationErrorResponse> errors) {
        this.errors = errors;
    }
}
