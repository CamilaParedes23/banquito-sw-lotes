package com.banquito.switchpagos.batch.exception;

import com.banquito.switchpagos.batch.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getCode(), exception.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
        UUID correlationId = UUID.randomUUID();
        LOG.error("Unexpected batch-service error. correlationId={}", correlationId, exception);
        ErrorResponse response = baseResponse(HttpStatus.INTERNAL_SERVER_ERROR, request);
        response.setCorrelationId(correlationId);
        response.setMessage("Ocurrio un error tecnico al procesar la solicitud.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {
        return buildResponse(status, null, message, request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String code,
            String message,
            HttpServletRequest request) {
        ErrorResponse response = baseResponse(status, request);
        response.setCorrelationId(UUID.randomUUID());
        response.setCode(code);
        response.setMessage(message);
        return ResponseEntity.status(status).body(response);
    }

    private ErrorResponse baseResponse(HttpStatus status, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(OffsetDateTime.now());
        response.setStatus(status.value());
        response.setError(status.getReasonPhrase());
        response.setPath(request.getRequestURI());
        return response;
    }
}
