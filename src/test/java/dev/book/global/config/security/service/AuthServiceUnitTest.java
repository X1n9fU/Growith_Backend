package dev.book.global.config.security.service;

import dev.book.accountbook.type.Category;
import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.config.security.dto.TokenDto;
import dev.book.global.config.security.exception.AuthErrorCode;
import dev.book.global.config.security.exception.AuthException;
import dev.book.global.config.security.jwt.JwtAuthenticationToken;
import dev.book.global.config.security.jwt.JwtUtil;
import dev.book.global.config.security.service.refresh.RefreshTokenService;
import dev.book.user.dto.request.UserSignUpRequest;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserErrorCode;
import dev.book.user.exception.UserErrorException;
import dev.book.user.repository.UserRepository;
import dev.book.util.UserBuilder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    CustomUserDetails userDetails;

    @BeforeEach
    public void createUser(){
        UserEntity user = UserBuilder.of();
        userDetails = new CustomUserDetails(user);
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }

    @Test
    @DisplayName("OAuth2를 통해 접근한 유저를 회원가입 한다.")
    void signUp() {
        String nickname = "nickname";
        List<Category> categories = List.of(Category.CONVENIENCE_STORE);

        //given
        UserEntity user = UserBuilder.of();
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest(nickname, categories);
        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
        given(userRepository.existsByNickname(any())).willReturn(false);
        given(userDetailsService.loadUserByUsername(any())).willReturn(userDetails);
        given(jwtUtil.generateToken(any(), any())).willReturn(new TokenDto("access_token", "refresh_token"));

        //when
        authService.signUp(userSignUpRequest, user.getEmail(), response);

        //then
        assertEquals(nickname, user.getNickname());
        assertEquals(categories, user.getUserCategory());
        verify(refreshTokenService).saveAndUpdateRefreshToken(user, "refresh_token");
    }

    @Test
    @DisplayName("닉네임이 중복된 경우 에러가 발생한다.")
    void validateNickname() {
        //given
        String nickname = "test@test.com";
        given(userRepository.existsByNickname(any())).willReturn(true);

        //when
        assertThatThrownBy(() -> authService.validateNickname(nickname))
                .isInstanceOf(UserErrorException.class)
                .hasMessageContaining(UserErrorCode.DUPLICATE_NICKNAME.getMessage());
    }

    @Test
    @DisplayName("토큰을 삭제하여 유저를 로그아웃 시킨다.")
    void logout() {
        //given
        given(userRepository.save(any(UserEntity.class))).willReturn(userDetails.user());

        //when
        authService.logout(request, response, userDetails);

        assertThat(userDetails.user().getRefreshToken()).isNull();
        verify(jwtUtil).deleteAccessTokenAndRefreshToken(request, response);
    }

    @Test
    @DisplayName("토큰 재발급 과정을 검증한다.")
    void reissueToken() {
        //given
        String refreshToken = "refresh_token";
        //인증 객체 생성
        UserEntity user = userDetails.user();
        Authentication authentication = new JwtAuthenticationToken(userDetails, userDetails.getAuthorities());
        given(userRepository.findByEmail(any())).willReturn(Optional.ofNullable(user));
        given(userDetailsService.loadUserByUsername(any())).willReturn(userDetails);
        given(jwtUtil.validateToken(any())).willReturn(refreshToken);
        given(jwtUtil.getRefreshToken(any())).willReturn(refreshToken);

        //when
        authService.reissueToken(request, response);

        //then
        verify(jwtUtil).getRefreshToken(request);
        verify(jwtUtil).validateToken(refreshToken);
        verify(jwtUtil).generateAccessToken(response, authentication);
    }

    @Test
    @DisplayName("만료된 토큰이라면 에러를 반환한다.")
    void expiredTokenError(){
        //given
        String expiredToken = "expired_token";
        given(jwtUtil.getRefreshToken(request)).willReturn(expiredToken);
        given(jwtUtil.validateToken(expiredToken)).willThrow(ExpiredJwtException.class);

        //when, then
        assertThatThrownBy(() -> authService.reissueToken(request, response))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(AuthErrorCode.EXPIRED_JWT_TOKEN.getMessage());
    }

    @Test
    @DisplayName("토큰이 올바르지 않다면 에러를 반환한다.")
    void invalidTokenError(){
        //given
        String refreshToken = "";
        given(jwtUtil.getRefreshToken(request)).willReturn(refreshToken);
        given(jwtUtil.validateToken(refreshToken)).willThrow(MalformedJwtException.class);

        //when, then
        assertThatThrownBy(() -> authService.reissueToken(request, response))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(AuthErrorCode.INVALID_JWT_TOKEN.getMessage());

    }

    @Test
    @DisplayName("토큰이 존재하지 않다면 에러를 발생한다.")
    void nullToken(){
        //given
        given(jwtUtil.getRefreshToken(request)).willReturn(null);
        given(jwtUtil.validateToken(null)).willThrow(JwtException.class);

        //when, then
        assertThatThrownBy(() -> authService.reissueToken(request, response))
                .isInstanceOf(JwtException.class);
    }

}