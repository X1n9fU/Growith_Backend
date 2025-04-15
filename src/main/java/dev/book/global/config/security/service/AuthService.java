package dev.book.global.config.security.service;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.config.security.dto.TokenDto;
import dev.book.global.config.security.jwt.JwtAuthenticationToken;
import dev.book.global.config.security.jwt.JwtUtil;
import dev.book.global.config.security.service.refresh.RefreshTokenService;
import dev.book.user.dto.request.UserSignUpRequest;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserErrorCode;
import dev.book.user.exception.UserErrorException;
import dev.book.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * 유저의 회원가입을 진행합니다.
     * @param userSignupRequest
     * @param response
     */
    @Transactional
    public void signUp(UserSignUpRequest userSignupRequest, HttpServletResponse response) {
        UserEntity user = userRepository.findByEmail(userSignupRequest.email())
                .orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new JwtAuthenticationToken(userDetails, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        validateNickname(userSignupRequest.nickname());

        user.updateNickname(userSignupRequest.nickname());
        user.updateCategory(userSignupRequest.category());
        userRepository.save(user);

        TokenDto tokenDto = getTokenDto(response, authentication);

        refreshTokenService.saveAndUpdateRefreshToken(user, tokenDto.refreshToken());
    }


    private TokenDto getTokenDto(HttpServletResponse response, Authentication authentication) {
        TokenDto tokenDto = jwtUtil.generateToken(authentication);
        jwtUtil.addTokenInCookie(response, tokenDto);
        return tokenDto;
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
        userDetails.user().deleteRefreshToken();
        jwtUtil.deleteAccessTokenAndRefreshToken(request, response); //쿠키에서 refreshToken 지우기
        userRepository.save(userDetails.user());
    }
}
