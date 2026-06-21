package com.banquito.switchpagos.batch.exception;

public class BadRequestException extends RuntimeException {

    private final String code;

    public BadRequestException(String message) {
        super(message);
        this.code = null;
    }

    public BadRequestException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
        this.code = null;
    }

    public String getCode() {
        return code;
    }
}
