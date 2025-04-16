package dev.book.challenge.dto.response;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.type.Status;

public record ChallengeReadResponse(Long id, String title,
                                    Integer capacity, Status status) {

    public static ChallengeReadResponse fromEntity(Challenge challenge) {
        return new ChallengeReadResponse(challenge.getId(), challenge.getTitle(), challenge.getAmount(), challenge.getStatus());
    }
}
