package dev.book.challenge.dto.response;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.type.Period;
import dev.book.challenge.type.Status;

public record ChallengeReadResponse(Long id, String title, Period period,
                                    Integer capacity, Status status) {

    public static ChallengeReadResponse fromEntity(Challenge challenge) {
        return new ChallengeReadResponse(challenge.getId(), challenge.getTitle(), challenge.getPeriod(), challenge.getAmount(), challenge.getStatus());
    }
}
