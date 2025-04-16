package dev.book.challenge.dto.response;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.type.Category;
import dev.book.challenge.type.Release;
import dev.book.challenge.type.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ChallengeUpdateResponse(Long id, String title, String text, Release release,
                                      Integer amount,
                                      Integer capacity, Category category, Status status,
                                      LocalDate startDate, LocalDate endDate,
                                      LocalDateTime createDate,
                                      LocalDateTime modifyDate) {

    public static ChallengeUpdateResponse fromEntity(Challenge challenge) {
        return new ChallengeUpdateResponse(challenge.getId(), challenge.getTitle(),
                challenge.getText(), challenge.getRelease(), challenge.getAmount(),
                challenge.getCapacity(), challenge.getCategory(), challenge.getStatus(),
                challenge.getStartDate(), challenge.getEndDate(), challenge.getCreatedAt(),
                challenge.getUpdatedAt());
    }
}
