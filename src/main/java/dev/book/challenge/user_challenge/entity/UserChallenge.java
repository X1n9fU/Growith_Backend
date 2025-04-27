package dev.book.challenge.user_challenge.entity;

import dev.book.challenge.entity.Challenge;
import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private boolean isSuccess = false;

    private boolean isWriteTip = false;

    private UserChallenge(UserEntity user, Challenge challenge) {
        this.user = user;
        this.challenge = challenge;
    }

    public static UserChallenge of(UserEntity user, Challenge challenge) {
        return new UserChallenge(user, challenge);
    }

    public void success(){
        this.isSuccess = true;
    }

    public void writeTip(){
        this.isWriteTip = true;
    }
}
