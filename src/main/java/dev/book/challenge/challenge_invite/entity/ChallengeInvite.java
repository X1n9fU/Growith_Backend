package dev.book.challenge.challenge_invite.entity;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.exception.ChallengeException;
import dev.book.global.entity.BaseTimeEntity;
import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static dev.book.challenge.exception.ErrorCode.CHALLENGE_ALREADY_ACCEPT;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChallengeInvite extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_user_id")
    private UserEntity requestUser;
    @ManyToOne
    @JoinColumn(name = "invite_user_id")
    private UserEntity inviteUser;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private boolean isRequest;

    private boolean isAccept;

    private ChallengeInvite(UserEntity requestUser, UserEntity inviteUser, Challenge challenge) {
        this.requestUser = requestUser;
        this.inviteUser = inviteUser;
        this.challenge = challenge;
        this.isRequest = true;
    }

    public static ChallengeInvite of(UserEntity requestUser, UserEntity inviteUser, Challenge challenge) {
        return new ChallengeInvite(requestUser, inviteUser, challenge);
    }

    public void accept() {
        this.isAccept = true;
        this.inviteUser.plusChallengeCount();
        this.challenge.plusCurrentCapacity();
    }

    public void reject() {
        if (this.isAccept) {
            throw new ChallengeException(CHALLENGE_ALREADY_ACCEPT);
        }
    }
}
