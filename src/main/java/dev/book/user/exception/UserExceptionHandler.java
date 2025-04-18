package dev.book.user.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserErrorException.class)
    public ResponseEntity<?> handleUserErrorException(UserErrorException e){
        UserErrorCode errorCode = e.getUserErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(errorCode.getMessage());
    }
}
