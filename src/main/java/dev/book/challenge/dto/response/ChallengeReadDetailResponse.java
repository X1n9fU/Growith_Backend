package dev.book.challenge.dto.response;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.type.ChallengeCategory;
import dev.book.challenge.type.Release;
import dev.book.challenge.type.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ChallengeReadDetailResponse(Long id, String creator, String title, Release release, Integer amount,
                                          Integer capacity, ChallengeCategory challengeCategory, Status status, LocalDate startDate,
                                          LocalDate endDate, LocalDateTime createDate,
                                          LocalDateTime modifyDate) {

    public static ChallengeReadDetailResponse fromEntity(Challenge challenge) {
        return new ChallengeReadDetailResponse(challenge.getId(), challenge.getCreator().getName(), challenge.getTitle(), challenge.getRelease(), challenge.getAmount(), challenge.getCapacity(), challenge.getChallengeCategory(), challenge.getStatus(), challenge.getStartDate(), challenge.getEndDate(), challenge.getCreatedAt(), challenge.getUpdatedAt());
    }
}
