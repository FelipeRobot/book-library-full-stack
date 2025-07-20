package com.booklibrary.exception;

import jakarta.ejb.EJBException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EJBExceptionMapperTest {

    @Test
    public void testToResponse_EJBExceptionWithValidationExceptionCause_ShouldReturn400BadRequest() {
        // Given
        EJBExceptionMapper mapper = new EJBExceptionMapper();
        ValidationException validationException = new ValidationException("Book title is required");
        EJBException ejbException = new EJBException(validationException);

        // When
        Response response = mapper.toResponse(ejbException);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("application/json", response.getMediaType().toString());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        
        assertNotNull(responseBody);
        assertEquals("Validation Error", responseBody.get("error"));
        assertEquals("Book title is required", responseBody.get("message"));
        assertEquals("error", responseBody.get("status"));
        assertEquals(400, responseBody.get("code"));
    }

    @Test
    public void testToResponse_EJBExceptionWithNotFoundExceptionCause_ShouldReturn404NotFound() {
        // Given
        EJBExceptionMapper mapper = new EJBExceptionMapper();
        NotFoundException notFoundException = new NotFoundException("Book with ID 999 not found");
        EJBException ejbException = new EJBException(notFoundException);

        // When
        Response response = mapper.toResponse(ejbException);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("application/json", response.getMediaType().toString());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        
        assertNotNull(responseBody);
        assertEquals("Not Found", responseBody.get("error"));
        assertEquals("Book with ID 999 not found", responseBody.get("message"));
        assertEquals("error", responseBody.get("status"));
        assertEquals(404, responseBody.get("code"));
    }

    @Test
    public void testToResponse_EJBExceptionWithOtherExceptionCause_ShouldReturn500InternalServerError() {
        // Given
        EJBExceptionMapper mapper = new EJBExceptionMapper();
        RuntimeException runtimeException = new RuntimeException("Database connection failed");
        EJBException ejbException = new EJBException(runtimeException);

        // When
        Response response = mapper.toResponse(ejbException);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("application/json", response.getMediaType().toString());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        
        assertNotNull(responseBody);
        assertEquals("Internal Server Error", responseBody.get("error"));
        assertEquals("An unexpected error occurred", responseBody.get("message"));
        assertEquals("error", responseBody.get("status"));
        assertEquals(500, responseBody.get("code"));
    }

    @Test
    public void testToResponse_EJBExceptionWithNullCause_ShouldReturn500InternalServerError() {
        // Given
        EJBExceptionMapper mapper = new EJBExceptionMapper();
        EJBException ejbException = new EJBException();

        // When
        Response response = mapper.toResponse(ejbException);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        
        assertNotNull(responseBody);
        assertEquals("Internal Server Error", responseBody.get("error"));
        assertEquals("An unexpected error occurred", responseBody.get("message"));
        assertEquals("error", responseBody.get("status"));
        assertEquals(500, responseBody.get("code"));
    }

    @Test
    public void testToResponse_EJBExceptionWithStringCause_ShouldReturn500InternalServerError() {
        // Given
        EJBExceptionMapper mapper = new EJBExceptionMapper();
        EJBException ejbException = new EJBException("Simple error message");

        // When
        Response response = mapper.toResponse(ejbException);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        
        assertNotNull(responseBody);
        assertEquals("Internal Server Error", responseBody.get("error"));
        assertEquals("An unexpected error occurred", responseBody.get("message"));
        assertEquals("error", responseBody.get("status"));
        assertEquals(500, responseBody.get("code"));
    }
} 