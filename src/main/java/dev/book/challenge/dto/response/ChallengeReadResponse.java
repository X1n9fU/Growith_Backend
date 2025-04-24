package dev.book.challenge.dto.response;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.type.Status;

public record ChallengeReadResponse(Long id, String title,
                                    Integer capacity, Integer participants, Status status) {

    public static ChallengeReadResponse fromEntity(Challenge challenge) {
        return new ChallengeReadResponse(challenge.getId(), challenge.getTitle(), challenge.getCapacity(), challenge.getCurrentCapacity(), challenge.getStatus());
    }
}
