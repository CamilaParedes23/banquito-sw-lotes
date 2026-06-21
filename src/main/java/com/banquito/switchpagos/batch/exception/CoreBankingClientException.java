package com.banquito.switchpagos.batch.exception;

public class CoreBankingClientException extends RuntimeException {

    private final Integer httpStatus;
    private final Boolean functionalRejection;

    public CoreBankingClientException(String message) {
        super(message);
        this.httpStatus = null;
        this.functionalRejection = false;
    }

    public CoreBankingClientException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = null;
        this.functionalRejection = false;
    }

    public CoreBankingClientException(
            String message,
            Throwable cause,
            Integer httpStatus,
            Boolean functionalRejection) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.functionalRejection = functionalRejection;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public Boolean isFunctionalRejection() {
        return functionalRejection;
    }
}
