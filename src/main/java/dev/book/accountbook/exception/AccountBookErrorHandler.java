package dev.book.accountbook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class AccountBookErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrorException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    Map<String, String> fieldError = new HashMap<>();
                    fieldError.put("field", error.getField());
                    fieldError.put("message", error.getDefaultMessage());
                    fieldError.put("code", error.getCode());

                    return fieldError;
                }).toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation failed");
        response.put("errors", errorDetails);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AccountBookErrorException.class)
    public ResponseEntity<Map<String, Object>> handleAccountBookErrorException(AccountBookErrorException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", ex.getErrorCode().getStatus().value());
        response.put("message", ex.getErrorCode().getMessage());

        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }
}
