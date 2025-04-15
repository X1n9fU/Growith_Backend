package dev.book.challenge.entity;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.request.ChallengeUpdateRequest;
import dev.book.challenge.type.Category;
import dev.book.challenge.type.Release;
import dev.book.challenge.type.Status;
import dev.book.user.entity.UserEntity;
import dev.book.user_challenge.entity.UserChallenge;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "release_type")
    private Release release;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "challenge")
    private List<UserChallenge> userChallenges;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    private Challenge(String title, String text, String release, int amount, String category, Integer capacity, LocalDate startDate, LocalDate endDate, UserEntity creator) {
        this.title = title;
        this.text = text;
        this.release = Release.valueOf(release);
        this.amount = amount;
        this.capacity = capacity;
        this.status = Status.RECRUITING;
        this.category = Category.valueOf(category);
        this.startDate = startDate;
        this.endDate = endDate;
        this.creator = creator;
    }

    public static Challenge of(ChallengeCreateRequest challengeCreateRequest, UserEntity creator) {
        return new Challenge(challengeCreateRequest.title(), challengeCreateRequest.text(),
                challengeCreateRequest.release(),
                challengeCreateRequest.amount(), challengeCreateRequest.category(), challengeCreateRequest.capacity(),
                challengeCreateRequest.startDate(), challengeCreateRequest.endDate(), creator);
    }

    public void updateInfo(ChallengeUpdateRequest request) {
        this.title = request.title();
        this.text = request.text();
        this.release = Release.valueOf(request.release());
        this.amount = request.amount();
        this.capacity = request.capacity();
        this.status = Status.RECRUITING;
        this.category = Category.valueOf(request.category());
        this.startDate = request.startDate();
        this.endDate = request.endDate();
        this.modifyDate = LocalDateTime.now();
    }
}
