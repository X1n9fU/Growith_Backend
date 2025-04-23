package dev.book.challenge.dto.response;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.type.Status;

public record ChallengeReadResponse(Long id, String title,
                                    Integer capacity, int participants, Status status) {

    public static ChallengeReadResponse fromEntity(Challenge challenge,int participants) {
        return new ChallengeReadResponse(challenge.getId(), challenge.getTitle(), challenge.getAmount(), participants,challenge.getStatus());
    }
}
