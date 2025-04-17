package dev.book.global.config.security.handler;

import dev.book.user_friend.service.UserFriendService;
import dev.book.global.config.security.jwt.JwtUtil;
import dev.book.global.config.security.service.oauth2.OAuth2AuthService;
import dev.book.global.config.security.util.CookieUtil;
import dev.book.user.enums.UserLoginState;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String REQUEST_USER_TOKEN = "request_user_token";

    private final OAuth2AuthService oauth2AuthService;
    private final JwtUtil jwtUtil;
    private final UserFriendService userFriendService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String invitationToken = extractInvitationToken(request);
        UserLoginState userLoginState = oauth2AuthService.getAttributes(authentication);

        // 친구 요청 처리
        handleFriendInvitation(authentication, invitationToken);

        switch (userLoginState){
            case LOGIN_SUCCESS -> {
                jwtUtil.generateToken(response, authentication);
                getRedirectStrategy().sendRedirect(request, response, "/main");
            }
            case PROFILE_INCOMPLETE ->
                getRedirectStrategy().sendRedirect(request, response, "/signup?email"+authentication.getName());
        }
    }

    private String extractInvitationToken(HttpServletRequest request) {
        return CookieUtil.getCookie(request, REQUEST_USER_TOKEN);
    }

    private void handleFriendInvitation(Authentication authentication, String invitationToken) {
        if (invitationToken == null) return;

        try {
            userFriendService.makeInvitation(authentication.getName(), invitationToken);
        } catch (Exception e) {
            throw new RuntimeException("친구 요청 처리 중 오류 발생", e);
        }
    }
}
