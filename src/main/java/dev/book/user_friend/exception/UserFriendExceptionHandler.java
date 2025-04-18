package dev.book.user_friend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class UserFriendExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserFriendException.class)
    public ResponseEntity<?> handlerUserFriendException(UserFriendException e){
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(e.getMessage());
    }
}
