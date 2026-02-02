package com.azeroth.api.exception;

import com.azeroth.api.enums.ErrorCode;

public class BussinesException extends RuntimeException {

    private ErrorCode errorCode;

    public BussinesException(String message) {
        super(message);
    }

    public BussinesException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
