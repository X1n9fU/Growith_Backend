package dev.book.challenge.entity;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.type.Category;
import dev.book.challenge.type.Period;
import dev.book.challenge.type.Release;
import dev.book.challenge.type.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Period period;

    @Enumerated(EnumType.STRING)
    @Column(name = "release_type")
    private Release release;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    private Challenge(String title, String period, String release, int amount, String status, String category, Integer capacity) {
        this.title = title;
        this.period = Period.valueOf(period);
        this.release = Release.valueOf(release);
        this.amount = amount;
        this.capacity = capacity;
        this.status = Status.valueOf(status);
        this.category = Category.valueOf(category);
    }

    public static Challenge of(ChallengeCreateRequest challengeCreateRequest) {
        return new Challenge(challengeCreateRequest.title(),
                challengeCreateRequest.period(), challengeCreateRequest.release(),
                challengeCreateRequest.amount(), challengeCreateRequest.status(), challengeCreateRequest.category(), challengeCreateRequest.capacity());
    }

}
