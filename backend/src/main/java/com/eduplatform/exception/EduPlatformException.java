package com.eduplatform.exception;

import lombok.Getter;
import java.util.Map;

@Getter
public abstract class EduPlatformException extends RuntimeException {
    private final String errorCode;
    private final Map<String, Object> details;

    protected EduPlatformException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    protected EduPlatformException(String message, String errorCode, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    protected EduPlatformException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = null;
    }
}