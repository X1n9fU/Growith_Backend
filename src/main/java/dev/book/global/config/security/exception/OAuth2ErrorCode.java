package dev.book.global.config.security.exception;

import dev.book.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
//public class CustomOAuth2Error extends OAuth2Error {
//
//    private final int httpCode;
//
//    public CustomOAuth2Error(String errorCode, String description, String uri, int httpCode) {
//        super(errorCode, description, uri);
//        this.httpCode = httpCode;
//    }
//}

@RequiredArgsConstructor
public enum OAuth2ErrorCode implements ErrorCode {

    UNVALIDATED_PROVIDER(HttpStatus.FORBIDDEN, "지원하지 않는 공급자입니다.");

    private final HttpStatus status;
    private final String message;
}