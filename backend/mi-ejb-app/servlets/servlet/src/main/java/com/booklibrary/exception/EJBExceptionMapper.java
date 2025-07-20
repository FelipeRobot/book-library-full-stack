package com.booklibrary.exception;

import jakarta.ejb.EJBException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapea EJBException a respuestas HTTP apropiadas
 */
@Provider
public class EJBExceptionMapper implements ExceptionMapper<EJBException> {
    
    @Override
    public Response toResponse(EJBException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        
        // Obtener la causa de la excepción
        Throwable cause = exception.getCause();
        
        // Si la causa es una ValidationException, devolver 400
        if (cause instanceof ValidationException) {
            errorResponse.put("error", "Validation Error");
            errorResponse.put("message", cause.getMessage());
            errorResponse.put("status", "error");
            errorResponse.put("code", 400);
            
            return Response.status(Response.Status.BAD_REQUEST)
                          .entity(errorResponse)
                          .type(MediaType.APPLICATION_JSON)
                          .build();
        }
        
        // Si la causa es una NotFoundException, devolver 404
        if (cause instanceof NotFoundException) {
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", cause.getMessage());
            errorResponse.put("status", "error");
            errorResponse.put("code", 404);
            
            return Response.status(Response.Status.NOT_FOUND)
                          .entity(errorResponse)
                          .type(MediaType.APPLICATION_JSON)
                          .build();
        }
        
        // Para otras excepciones, devolver 500
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