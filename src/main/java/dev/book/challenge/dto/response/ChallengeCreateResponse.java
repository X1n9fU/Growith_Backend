package dev.book.challenge.dto.response;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.type.Category;
import dev.book.challenge.type.Period;
import dev.book.challenge.type.Release;
import dev.book.challenge.type.Status;

import java.time.LocalDateTime;

public record ChallengeCreateResponse(Long id, String title, Period period, Release release, Integer amount,
                                      Integer capacity, Category category, Status status, LocalDateTime createDate,
                                      LocalDateTime modifyDate) {

    public static ChallengeCreateResponse fromEntity(Challenge challenge) {
        return new ChallengeCreateResponse(challenge.getId(), challenge.getTitle(), challenge.getPeriod(), challenge.getRelease(), challenge.getAmount(), challenge.getCapacity(), challenge.getCategory(), challenge.getStatus(), challenge.getCreateDate(), challenge.getModifyDate());
    }
}
