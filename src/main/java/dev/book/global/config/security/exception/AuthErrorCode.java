package dev.book.global.config.security.exception;

import dev.book.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    UNVALIDATED_PROVIDER(HttpStatus.FORBIDDEN, "지원하지 않는 공급자입니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED,"존재하지 않거나 잘못된 JWT 토큰 형식입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다.");

    private final HttpStatus status;
    private final String message;
}