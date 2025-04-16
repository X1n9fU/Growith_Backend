package dev.book.global.config.security.handler;

import dev.book.global.config.security.dto.TokenDto;
import dev.book.global.config.security.jwt.JwtUtil;
import dev.book.global.config.security.service.oauth2.OAuth2AuthService;
import dev.book.user.enums.UserLoginState;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
OAuth2 로그인 성공 시
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2AuthService oauth2AuthService;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        //가져온 인증 정보를 통해 유저 존재 여부 확인 후 리다이렉션 진행
        UserLoginState userLoginState = oauth2AuthService.getAttributes(authentication);

        switch(userLoginState){ //todo 반환되는 화면 경로 정하기
            case LOGIN_SUCCESS ->{
                jwtUtil.generateToken(response, authentication);
                getRedirectStrategy().sendRedirect(request, response, "/main");
            }
            case PROFILE_INCOMPLETE ->
                getRedirectStrategy().sendRedirect(request, response, "/signup");
        }
    }
}
