package dev.book.global.config.security.exception;

import dev.book.global.exception.CustomErrorException;
import dev.book.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import javax.naming.AuthenticationException;

@Getter
public class OAuth2Exception extends CustomErrorException {
    public OAuth2Exception(OAuth2ErrorCode errorCode){
        super(errorCode.getMessage(), errorCode);
    }
}
