package dev.book.user_challenge.entity;

import dev.book.challenge.dummy.DummyUser;
import dev.book.challenge.entity.Challenge;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DummyUser user;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private UserChallenge(DummyUser user, Challenge challenge) {
        this.user = user;
        this.challenge = challenge;
    }

    public static UserChallenge of(DummyUser user, Challenge challenge) {
        return new UserChallenge(user, challenge);
    }
}
