package dev.book.user.user_friend.dto.response;

import dev.book.user.entity.UserEntity;
import lombok.Builder;

@Builder
public record FriendListResponseDto(
        Long friendUserId,
        String name,
        String profileImageUrl,

        int participatingChallenges
){
    public static FriendListResponseDto of(UserEntity friend) {
        return FriendListResponseDto.builder()
                .friendUserId(friend.getId())
                .name(friend.getName())
                .profileImageUrl(friend.getProfileImageUrl())
                .participatingChallenges(friend.getParticipatingChallenges())
                .build();
    }
}
