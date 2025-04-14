package dev.book.challenge.dto.response;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.type.Category;
import dev.book.challenge.type.Period;
import dev.book.challenge.type.Release;
import dev.book.challenge.type.Status;

import java.time.LocalDateTime;

public record ChallengeReadDetailResponse(Long id, String title, Period period, Release release, Integer amount,
                                          Integer capacity, Category category, Status status, LocalDateTime createDate,
                                          LocalDateTime modifyDate) {

    public static ChallengeReadDetailResponse fromEntity(Challenge challenge) {
        return new ChallengeReadDetailResponse(challenge.getId(), challenge.getTitle(), challenge.getPeriod(), challenge.getRelease(), challenge.getAmount(), challenge.getCapacity(), challenge.getCategory(), challenge.getStatus(), challenge.getCreateDate(), challenge.getModifyDate());
    }
}
