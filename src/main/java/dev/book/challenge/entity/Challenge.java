package dev.book.challenge.entity;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.request.ChallengeUpdateRequest;
import dev.book.challenge.exception.ChallengeException;
import dev.book.challenge.type.Category;
import dev.book.challenge.type.Release;
import dev.book.challenge.type.Status;
import dev.book.challenge.user_challenge.entity.UserChallenge;
import dev.book.global.entity.BaseTimeEntity;
import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

import static dev.book.challenge.exception.ErrorCode.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Challenge extends BaseTimeEntity {
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

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChallenge> userChallenges;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

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
    }

    public void isOver(long countParticipants) {
        if (countParticipants >= this.capacity) {
            throw new ChallengeException(CHALLENGE_CAPACITY_FULL);
        }
    }

    public void checkAlreadyStartOrEnd() {
        checkAlreadyStart();
        checkAlreadyEnd();

    }

    private void checkAlreadyStart() {
        if (Status.IN_PROGRESS.equals(this.status)) {
            throw new ChallengeException(CHALLENGE_ALREADY_START);
        }
    }

    private void checkAlreadyEnd() {
        if (Status.COMPLETED.equals(this.status)) {
            throw new ChallengeException(CHALLENGE_ALREADY_END);
        }
    }
}
