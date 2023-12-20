package combat.squad.auth.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {

        HttpStatus status = ex.getStatus();
        String error = status.getReasonPhrase();
        String message = ex.getReason();
        LocalDateTime timestamp = LocalDateTime.now();

        ErrorResponse errorResponse = new ErrorResponse(timestamp, status.value(), error, message);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String error = status.getReasonPhrase();
        LocalDateTime timestamp = LocalDateTime.now();

        Map<String, String> validationErrors = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(timestamp, status.value(), error, "Validation failed");

        if (!validationErrors.isEmpty()) {
            errorResponse.setValidationErrors(validationErrors);
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String error = status.getReasonPhrase();
        LocalDateTime timestamp = LocalDateTime.now();

        ErrorResponse errorResponse = new ErrorResponse(timestamp, status.value(), error, "Invalid JSON format, probably invalid date format");

        return new ResponseEntity<>(errorResponse, status);
    }

    @Getter
    private static class ErrorResponse {

        private final LocalDateTime timestamp;
        private final int status;
        private final String error;
        private final String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Map<String, String> validationErrors;

        public ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
        }

        public void setValidationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
        }
    }
}
