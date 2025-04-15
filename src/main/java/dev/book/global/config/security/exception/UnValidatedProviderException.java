package dev.book.global.config.security.exception;

import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Getter
public class UnValidatedProviderException extends OAuth2AuthenticationException {

    private final int httpCode;

    public UnValidatedProviderException(CustomOAuth2Error oAuth2Error){
        super(oAuth2Error);
        this.httpCode = oAuth2Error.getHttpCode();
    }
}
