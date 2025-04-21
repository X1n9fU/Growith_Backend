package dev.book.global.config.security.service;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.config.security.dto.TokenDto;
import dev.book.global.config.security.exception.AuthErrorCode;
import dev.book.global.config.security.exception.AuthException;
import dev.book.global.config.security.jwt.JwtAuthenticationToken;
import dev.book.global.config.security.jwt.JwtUtil;
import dev.book.global.config.security.service.refresh.RefreshTokenService;
import dev.book.global.entity.Category;
import dev.book.global.repository.CategoryRepository;
import dev.book.user.dto.request.UserSignUpRequest;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserErrorCode;
import dev.book.user.exception.UserErrorException;
import dev.book.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * 유저의 회원가입을 진행합니다.
     *
     * @param userSignupRequest
     * @param email
     * @param response
     */
    @Transactional
    public void signUp(UserSignUpRequest userSignupRequest, String email, HttpServletResponse response) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));
        Authentication authentication = getAuthentication(user);

        validateNickname(userSignupRequest.nickname());
        updateNickNameAndCategory(user, userSignupRequest);

        TokenDto tokenDto = getTokenDto(response, authentication);
        refreshTokenService.saveAndUpdateRefreshToken(user, tokenDto.refreshToken());
    }

    private void updateNickNameAndCategory(UserEntity user, UserSignUpRequest userSignupRequest) {
        user.updateNickname(userSignupRequest.nickname());
        List<Category> categories = categoryRepository.findByCategoryIn(userSignupRequest.categories());
        user.updateCategory(categories);
    }

    private Authentication getAuthentication(UserEntity user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new JwtAuthenticationToken(userDetails, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }


    private TokenDto getTokenDto(HttpServletResponse response, Authentication authentication) {
        return jwtUtil.generateToken(response, authentication);
    }

    public void validateNickname(String nickname) {
        boolean isExisted = userRepository.existsByNickname(nickname);
        if (isExisted) throw new UserErrorException(UserErrorCode.DUPLICATE_NICKNAME);
    }

    /**
     * RefreshToken 삭제
     * @param userDetails
     */
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, CustomUserDetails userDetails) {
        jwtUtil.deleteAccessTokenAndRefreshToken(request, response); //쿠키에서 refreshToken 지우기
        refreshTokenService.deleteRefreshToken(userDetails.user());
    }

    /**
     * refreshToken을 통한 accessToken 재발급
     *
     * @param request
     * @param response
     */
    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshToken = jwtUtil.getRefreshToken(request);
            String email = jwtUtil.validateToken(refreshToken);

            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));

            Authentication authentication = getAuthentication(user);
            jwtUtil.generateAccessToken(response, authentication);
        } catch (ExpiredJwtException e){
            throw new AuthException(AuthErrorCode.EXPIRED_JWT_TOKEN);
        } catch (MalformedJwtException | JwtException | IllegalArgumentException e) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_TOKEN);
        }
    }
}
