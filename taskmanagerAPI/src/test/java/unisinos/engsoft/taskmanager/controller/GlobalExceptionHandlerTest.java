package unisinos.engsoft.taskmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-path");
    }

    @Test
    void handleResponseStatusException_returnsCorrectResponse() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        ResponseEntity<Map<String, Object>> response = handler.handleResponseStatusException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("status")).isEqualTo(404);
        assertThat(body.get("error")).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(body.get("message")).isEqualTo("Resource not found");
        assertThat(body.get("path")).isEqualTo("/test-path");
        assertThat(body.get("timestamp")).isInstanceOf(String.class);
    }

    @Test
    void handleValidationExceptions_returnsCorrectResponse() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidationExceptions(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("status")).isEqualTo(400);
        assertThat(body.get("error")).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.get("message")).isEqualTo("Validation error");
        assertThat(body.get("path")).isEqualTo("/test-path");
        assertThat(body.get("timestamp")).isInstanceOf(String.class);

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertThat(errors).containsEntry("field", "must not be blank");
    }
}
