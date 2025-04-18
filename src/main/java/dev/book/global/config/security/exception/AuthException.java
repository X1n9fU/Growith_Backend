package dev.book.global.config.security.exception;

import dev.book.global.exception.CustomErrorException;
import lombok.Getter;

@Getter
public class AuthException extends CustomErrorException {
    public AuthException(AuthErrorCode errorCode){
        super(errorCode.getMessage(), errorCode);
    }
}
