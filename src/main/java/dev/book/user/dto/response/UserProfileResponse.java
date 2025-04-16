package dev.book.user.dto.response;

import dev.book.user.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record UserProfileResponse(

        @NotBlank
        String nickname,

        @Email @NotNull
        String email,

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
