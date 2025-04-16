package dev.book.global.config.security.dto.oauth2;

import lombok.Builder;

import java.util.Map;

public record OAuth2Attributes(String nickname, String email, String profileImageUrl) {

    @Builder
    public OAuth2Attributes {
    }

    public static OAuth2Attributes toKakao(Map<String, Object> attributes) {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
        String profileImageUrl = (String) profile.get("profile_image_url");
        String nickname = (String) profile.get("nickname");
        String email = (String) kakao_account.get("email");

        return OAuth2Attributes.builder()
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
