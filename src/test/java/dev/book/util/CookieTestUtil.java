package dev.book.util;

import dev.book.global.config.security.dto.TokenDto;
import jakarta.servlet.http.Cookie;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;

public class CookieTestUtil {

    public static TokenDto getTokenFromCookie(MockHttpServletResponse response){
        String accessToken = Arrays.stream(response.getCookies())
                .filter(cookie -> "access_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        String refreshToken = Arrays.stream(response.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        return new TokenDto(accessToken, refreshToken);
    }
}
