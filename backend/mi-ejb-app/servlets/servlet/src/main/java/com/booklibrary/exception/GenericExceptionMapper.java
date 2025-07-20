package com.booklibrary.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapea excepciones generales a respuestas HTTP 500 (Internal Server Error)
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred");
        errorResponse.put("status", "error");
        errorResponse.put("code", 500);
        
        // En producción, no deberíamos exponer detalles de la excepción
        // errorResponse.put("details", exception.getMessage());
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                      .entity(errorResponse)
                      .type(MediaType.APPLICATION_JSON)
                      .build();
    }
} 