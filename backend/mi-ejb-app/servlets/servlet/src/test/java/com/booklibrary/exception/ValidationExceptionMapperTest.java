package com.booklibrary.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationExceptionMapperTest {

    @Test
    public void testToResponse_ValidationException_ShouldReturn400BadRequest() {
        // Given
        ValidationExceptionMapper mapper = new ValidationExceptionMapper();
        ValidationException exception = new ValidationException("Book title is required");

        // When
        Response response = mapper.toResponse(exception);

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
    public void testToResponse_ValidationExceptionWithNullMessage_ShouldReturn400BadRequest() {
        // Given
        ValidationExceptionMapper mapper = new ValidationExceptionMapper();
        ValidationException exception = new ValidationException(null);

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        
        assertNotNull(responseBody);
        assertEquals("Validation Error", responseBody.get("error"));
        assertNull(responseBody.get("message"));
        assertEquals("error", responseBody.get("status"));
        assertEquals(400, responseBody.get("code"));
    }

    @Test
    public void testToResponse_ValidationExceptionWithEmptyMessage_ShouldReturn400BadRequest() {
        // Given
        ValidationExceptionMapper mapper = new ValidationExceptionMapper();
        ValidationException exception = new ValidationException("");

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        
        assertNotNull(responseBody);
        assertEquals("Validation Error", responseBody.get("error"));
        assertEquals("", responseBody.get("message"));
        assertEquals("error", responseBody.get("status"));
        assertEquals(400, responseBody.get("code"));
    }
} 