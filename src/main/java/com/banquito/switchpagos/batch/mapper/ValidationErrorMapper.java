package com.banquito.switchpagos.batch.mapper;

import com.banquito.switchpagos.batch.dto.response.ValidationErrorResponse;
import com.banquito.switchpagos.batch.model.BatchValidationError;
import org.springframework.stereotype.Component;

@Component
public class ValidationErrorMapper {

    public ValidationErrorResponse toResponse(BatchValidationError validationError) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.setCode(validationError.getCode());
        response.setField(validationError.getFieldName());
        response.setMessage(validationError.getMessage());
        return response;
    }
}
