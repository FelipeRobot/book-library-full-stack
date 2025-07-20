package com.booklibrary.exception;

/**
 * Excepción lanzada cuando hay errores de validación en los datos
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
} 