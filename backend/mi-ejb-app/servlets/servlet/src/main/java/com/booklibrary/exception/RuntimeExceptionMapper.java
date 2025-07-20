package com.booklibrary.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapea RuntimeException a respuestas HTTP apropiadas
 */
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    
    @Override
    public Response toResponse(RuntimeException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        
        // Si es una ValidationException, devolver 400
        if (exception instanceof ValidationException) {
            errorResponse.put("error", "Validation Error");
            errorResponse.put("message", exception.getMessage());
            errorResponse.put("status", "error");
            errorResponse.put("code", 400);
            
            return Response.status(Response.Status.BAD_REQUEST)
                          .entity(errorResponse)
                          .type(MediaType.APPLICATION_JSON)
                          .build();
        }
        
        // Si es una NotFoundException, devolver 404
        if (exception instanceof NotFoundException) {
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", exception.getMessage());
            errorResponse.put("status", "error");
            errorResponse.put("code", 404);
            
            return Response.status(Response.Status.NOT_FOUND)
                          .entity(errorResponse)
                          .type(MediaType.APPLICATION_JSON)
                          .build();
        }
        
        // Para otras RuntimeException, devolver 500
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred");
        errorResponse.put("status", "error");
        errorResponse.put("code", 500);
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                      .entity(errorResponse)
                      .type(MediaType.APPLICATION_JSON)
                      .build();
    }
} 