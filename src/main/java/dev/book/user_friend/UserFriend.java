package dev.book.user_friend;

import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "inviting_user_id")
    UserEntity invitingUser;

    @ManyToOne
    @JoinColumn(name = "invited_user_id")
    UserEntity invitedUser;

    Boolean request = false;

    Boolean accept = false;

    LocalDateTime requestedAt;

    LocalDateTime acceptedAt;

    @Builder
    public UserFriend(UserEntity invitingUser, UserEntity invitedUser, LocalDateTime requestedAt) {
        this.invitingUser = invitingUser;
        this.invitedUser = invitedUser;
        this.requestedAt = requestedAt;
        this.request = true;
    }

    public static UserFriend of(UserEntity invitingUser, LocalDateTime requestedAt) {
        return UserFriend.builder()
                .invitingUser(invitingUser)
                .requestedAt(requestedAt)
                .build();
    }

    public static UserFriend of(UserEntity invitingUser, UserEntity invitedUser, LocalDateTime requestedAt){
        return UserFriend.builder()
                .invitingUser(invitingUser)
                .invitedUser(invitedUser)
                .requestedAt(requestedAt)
                .build();
    }

    public void accept(){
        this.accept = true;
        this.acceptedAt = LocalDateTime.now();
    }

    public void updateInvitedUser(UserEntity invitedUser){
        this.invitedUser = invitedUser;
    }


}
