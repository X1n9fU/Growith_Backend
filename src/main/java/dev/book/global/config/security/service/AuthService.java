package dev.book.global.config.security.service;

import dev.book.global.config.security.dto.TokenDto;
import dev.book.global.config.security.jwt.JwtAuthenticationToken;
import dev.book.global.config.security.jwt.JwtUtil;
import dev.book.global.config.security.service.refresh.RefreshTokenService;
import dev.book.user.dto.request.UserSignUpRequest;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.DuplicateNicknameException;
import dev.book.user.exception.UserNotFoundException;
import dev.book.user.repository.UserRepository;
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
                .orElseThrow(() -> new UserNotFoundException("이메일에 해당하는 유저를 찾을 수 없습니다. :" + userSignupRequest.email()));

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
        response.addHeader("access_token", tokenDto.accessToken());
        response.addHeader("refresh_token", tokenDto.refreshToken());
        return tokenDto;
    }

    public void validateNickname(String nickname) {
        boolean isExisted = userRepository.existsByNickname(nickname);
        if (isExisted) throw new DuplicateNicknameException("이미 사용하고 있는 닉네임입니다. : " + nickname);
    }
}
