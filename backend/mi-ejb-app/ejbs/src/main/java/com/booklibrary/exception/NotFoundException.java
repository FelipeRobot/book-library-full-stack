package com.booklibrary.exception;

/**
 * Excepción lanzada cuando un recurso no se encuentra
 */
public class NotFoundException extends RuntimeException {
    
    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 