package dev.book.challenge.dto.response;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.type.Status;

public record ChallengeTopResponse(Long id, String title,
                                   Integer capacity, Long participants, Status status) {

    public static ChallengeTopResponse fromEntity(Challenge challenge, Long participants) {
        return new ChallengeTopResponse(challenge.getId(), challenge.getTitle(), challenge.getAmount(), participants, challenge.getStatus());
    }
}
