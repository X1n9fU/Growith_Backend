package dev.book.global.config.security.service;

import dev.book.global.config.security.exception.CustomOAuth2Error;
import dev.book.global.config.security.exception.UnValidatedProviderException;
import dev.book.user.entity.UserEntity;
import dev.book.user.enums.UserLoginState;
import dev.book.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * OAuth2User에서 필요한 정보를 가져온다.
 * - kakao
 *   profile_nickname, profile_image, account_email
 */
@Service
@RequiredArgsConstructor
public class Oauth2AuthService {

    private final UserService userService;

    public UserLoginState getAttributes(Authentication authentication, HttpServletResponse response){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (provider.equals("kakao")){
            return getKakaoAttributes(attributes);
        } else {
            CustomOAuth2Error oAuth2Error = new CustomOAuth2Error("Invalid_Provider", "지원되지 않는 공급자입니다.", null, 403);
            throw new UnValidatedProviderException(oAuth2Error);
        }
    }

    /**
     * attributes에서 필요한 정보들을 반환한다.
     * @param attributes
     * @return DB에서 유저를 조회하여 존재한다면 LOGIN_SUCCESS, 존재하지 않으면 현재 kakao 정보들만 저장한 후에 PROFILE_INCOMPLETE
     */
    private UserLoginState getKakaoAttributes(Map<String, Object> attributes) {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
        String profileImageUrl = (String) profile.get("profile_image_url");
        String nickname = (String) profile.get("nickname");
        String email = (String) kakao_account.get("email");

        if (userService.isUserExist(email)) return UserLoginState.LOGIN_SUCCESS;
        else {
            UserEntity newUser = UserEntity.builder()
                    .email(email)
                    .name(nickname)
                    .nickname("")
                    .profileImageUrl(profileImageUrl)
                    .build();
            userService.unComplete(newUser);
            return UserLoginState.PROFILE_INCOMPLETE;
        }
    }

}
