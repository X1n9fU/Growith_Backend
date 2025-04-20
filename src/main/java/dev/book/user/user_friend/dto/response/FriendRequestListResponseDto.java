package dev.book.user.user_friend.dto.response;

import dev.book.user.entity.UserEntity;
import lombok.Builder;

@Builder
public record FriendRequestListResponseDto (
        Long friendUserId,
        String name,
        String email,
        String profileImageUrl
){
    public static FriendRequestListResponseDto of(UserEntity friend) {
        return FriendRequestListResponseDto.builder()
                .friendUserId(friend.getId())
                .name(friend.getName())
                .profileImageUrl(friend.getProfileImageUrl())
                .email(friend.getEmail())
                .build();
    }
}
