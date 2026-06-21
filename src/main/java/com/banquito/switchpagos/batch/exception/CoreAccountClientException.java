package com.banquito.switchpagos.batch.exception;

public class CoreAccountClientException extends RuntimeException {

    private final String code;
    private final Integer httpStatus;

    public CoreAccountClientException(String code, String message) {
        this(code, message, null, null);
    }

    public CoreAccountClientException(String code, String message, Throwable cause, Integer httpStatus) {
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
