package dev.book.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 프로필 업데이트 RequestDto")
public record UserProfileUpdateRequest(
        @Schema(description = "변경할 유저 닉네임", defaultValue = "monchall")
        String nickname,
        @Schema(description = "변경할 유저 프로필 url",defaultValue = "https://~")
        String profileImageUrl) {
}
