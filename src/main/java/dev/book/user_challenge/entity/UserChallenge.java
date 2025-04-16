package dev.book.user_challenge.entity;

import dev.book.challenge.entity.Challenge;
import dev.book.user.entity.UserEntity;
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
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private UserChallenge(UserEntity user, Challenge challenge) {
        this.user = user;
        this.challenge = challenge;
    }

    public static UserChallenge of(UserEntity user, Challenge challenge) {
        return new UserChallenge(user, challenge);
    }
}
