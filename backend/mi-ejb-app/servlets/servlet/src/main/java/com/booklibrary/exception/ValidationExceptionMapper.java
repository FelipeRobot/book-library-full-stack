package com.booklibrary.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapea ValidationException a respuestas HTTP 400 (Bad Request)
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    
    @Override
    public Response toResponse(ValidationException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Validation Error");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("status", "error");
        errorResponse.put("code", 400);
        
        return Response.status(Response.Status.BAD_REQUEST)
                      .entity(errorResponse)
                      .type(MediaType.APPLICATION_JSON)
                      .build();
    }
} 