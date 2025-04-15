package dev.book.global.config.security.exception;

import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2Error;

@Getter
public class CustomOAuth2Error extends OAuth2Error {

    private final int httpCode;

    public CustomOAuth2Error(String errorCode, String description, String uri, int httpCode) {
        super(errorCode, description, uri);
        this.httpCode = httpCode;
    }
}
