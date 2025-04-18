package dev.book.user.dto.response;

import dev.book.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record UserProfileResponse(

        @Schema(description = "유저 닉네임", defaultValue = "growith")
        @NotBlank
        String nickname,

        @Schema(description = "유저 이메일", defaultValue = "test@test.com")
        @Email @NotNull
        String email,

        @Schema(description = "유저 프로필 이미지 url", defaultValue = "http://k.kakaocdn.net/~")
        String profileImageUrl
) {
    @Builder
    public UserProfileResponse(String nickname, String email, String profileImageUrl){
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserProfileResponse fromEntity(UserEntity user){
        return UserProfileResponse.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
