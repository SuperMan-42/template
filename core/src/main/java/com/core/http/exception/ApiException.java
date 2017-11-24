package com.core.http.exception;

/**
 * Created by hpw on 17-11-23.
 */

public class ApiException extends RuntimeException {
    private int errorCode;

    public ApiException(int code, String msg) {
        super(msg);
        this.errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }
}