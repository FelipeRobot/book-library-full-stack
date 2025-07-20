package com.booklibrary.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NotFoundExceptionMapperTest {

    @Test
    public void testToResponse_NotFoundException_ShouldReturn404NotFound() {
        // Given
        NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
        NotFoundException exception = new NotFoundException("Book with ID 999 not found");

        // When
        Response response = mapper.toResponse(exception);

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
    public void testToResponse_NotFoundExceptionWithNullMessage_ShouldReturn404NotFound() {
        // Given
        NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
        NotFoundException exception = new NotFoundException(null);

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        
        assertNotNull(responseBody);
        assertEquals("Not Found", responseBody.get("error"));
        assertNull(responseBody.get("message"));
        assertEquals("error", responseBody.get("status"));
        assertEquals(404, responseBody.get("code"));
    }

    @Test
    public void testToResponse_NotFoundExceptionWithEmptyMessage_ShouldReturn404NotFound() {
        // Given
        NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
        NotFoundException exception = new NotFoundException("");

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        
        assertNotNull(responseBody);
        assertEquals("Not Found", responseBody.get("error"));
        assertEquals("", responseBody.get("message"));
        assertEquals("error", responseBody.get("status"));
        assertEquals(404, responseBody.get("code"));
    }
} 