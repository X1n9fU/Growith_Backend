package dev.book.challenge.dto.response;

import dev.book.challenge.challenge_invite.entity.ChallengeInvite;

import java.time.LocalDateTime;

public record ChallengeInviteResponse(Long id, String requestUsername, String challengeName, Boolean isAccept,
                                      LocalDateTime requestAt, LocalDateTime updateAt) {

    public static ChallengeInviteResponse fromEntity(ChallengeInvite challengeInvite) {
        return new ChallengeInviteResponse(challengeInvite.getId(), challengeInvite.getRequestUser().getName(), challengeInvite.getChallenge().getTitle(), challengeInvite.isAccept(), challengeInvite.getCreatedAt(), challengeInvite.getUpdatedAt());
    }
}
