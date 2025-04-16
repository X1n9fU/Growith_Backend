package dev.book.global.config.security.filter;

import dev.book.global.config.security.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CheckFriendInviteFilter extends OncePerRequestFilter {

    private static final String REQUEST_FRIEND_URI = "/api/v1/friends/request";
    private static final String REQUEST_USER_TOKEN = "request_user_token";
    private static final long EXPIRATION_TIME = 86400;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String redirect_uri = request.getRequestURI();

        if (redirect_uri.equals(REQUEST_FRIEND_URI)){
            String token = request.getParameter("token");

            if (token != null){
                addTokenInCookie(response, token);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void addTokenInCookie(HttpServletResponse response, String token) {
        CookieUtil.addCookie(response, REQUEST_USER_TOKEN, token, (int) EXPIRATION_TIME * 1000);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith(REQUEST_FRIEND_URI);
    }
}
