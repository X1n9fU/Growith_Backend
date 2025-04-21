package dev.book.achievement.exception;

import dev.book.global.exception.GlobalExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AchievementExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(AchievementException.class)
    public ResponseEntity<?> AchievementExceptionHandler(AchievementException e){
        return ResponseEntity.status(e.getAchievementErrorCode().getStatus())
                .body(e.getAchievementErrorCode().getMessage());
    }

}

