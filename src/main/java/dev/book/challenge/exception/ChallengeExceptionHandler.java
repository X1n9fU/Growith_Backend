package dev.book.challenge.exception;

import dev.book.challenge.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ChallengeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ChallengeException.class)
    public ResponseEntity<?> handleChallengeException(ChallengeException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(errorResponse);
    }
}
