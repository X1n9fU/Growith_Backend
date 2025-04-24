package dev.book.challenge.dto.response;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.type.Status;

public record ChallengeReadResponse(Long id, String title,
                                    Integer capacity, Long participants, Status status) {

    public static ChallengeReadResponse fromEntity(Challenge challenge, Long participants) {
        return new ChallengeReadResponse(challenge.getId(), challenge.getTitle(), challenge.getCapacity(), participants, challenge.getStatus());
    }
}
