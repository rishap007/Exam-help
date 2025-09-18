package com.eduplatform.exception;

import java.util.Map;

public class FileProcessingException extends EduPlatformException {
    
    public FileProcessingException(String message) {
        super(message, "FILE_PROCESSING_ERROR");
    }
    
    public FileProcessingException(String message, Map<String, Object> details) {
        super(message, "FILE_PROCESSING_ERROR", details);
    }
    
    public FileProcessingException(String message, Throwable cause) {
        super(message, "FILE_PROCESSING_ERROR", cause);
    }
    
    public FileProcessingException(String operation, String filename, String reason) {
        super(String.format("File operation '%s' failed for file '%s': %s", operation, filename, reason), 
              "FILE_PROCESSING_ERROR");
    }
}