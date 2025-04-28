package dev.book.global.config.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Base64;

public class CookieUtil {

    /**
     * 쿠키를 추가합니다.
     * @param response
     * @param name 쿠키 이름
     * @param value 쿠키 값
     * @param maxAge 유효 시간
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);
        cookie.setDomain("localhost");
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    /**
     * 삭제할 쿠키의 유효 시간을 0으로 설정하여 삭제합니다.
     * @param request
     * @param response
     * @param name 쿠키 이름
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return;

        for (Cookie cookie : cookies){
            if (cookie.getName().equals(name)) {
                cookie.setValue("");
                cookie.setMaxAge(0);
                cookie.setSecure(true);
                cookie.setPath("/");
                cookie.setAttribute("SameSite", "None");
                response.addCookie(cookie);
            }
        }
    }

    public static String getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;

        for (Cookie cookie : cookies){
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 쿠키를 역직렬화하여 객체를 반환합니다.
     * @param cookie
     * @param tClass
     * @return 역직렬화된 객체
     * @param <T>
     */
    public static <T> T deserialize(Cookie cookie, Class<T> tClass) {
        return tClass.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );

    }

    /**
     * 쿠키를 직렬화하여 객체를 저장합니다.
     * @param obj
     * @return 직렬화된 객체
     */
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize((Serializable) obj));
    }
}
