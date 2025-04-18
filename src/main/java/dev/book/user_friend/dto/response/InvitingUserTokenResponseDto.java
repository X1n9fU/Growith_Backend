package dev.book.user_friend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record InvitingUserTokenResponseDto(
        @Schema(description = "친구 초대 요청 토큰", defaultValue = "ecMqbPm4OZ58BI.....") String invitingUserToken) {
}
