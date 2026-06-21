package com.banquito.switchpagos.batch.exception;

public class CoreCustomerClientException extends RuntimeException {

    private final String code;
    private final Integer httpStatus;

    public CoreCustomerClientException(String code, String message) {
        super(message);
        this.code = code;
        this.httpStatus = null;
    }

    public CoreCustomerClientException(String code, String message, Throwable cause, Integer httpStatus) {
        super(message, cause);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }
}
