package com.booklibrary.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapea NotFoundException a respuestas HTTP 404 (Not Found)
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    
    @Override
    public Response toResponse(NotFoundException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Not Found");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("status", "error");
        errorResponse.put("code", 404);
        
        return Response.status(Response.Status.NOT_FOUND)
                      .entity(errorResponse)
                      .type(MediaType.APPLICATION_JSON)
                      .build();
    }
} 