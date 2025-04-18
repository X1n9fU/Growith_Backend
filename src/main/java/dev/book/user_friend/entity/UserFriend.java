package dev.book.user_friend.entity;

import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    UserEntity friend;

    Boolean isRequest = false;

    Boolean isAccept = false;

    LocalDateTime requestedAt;

    LocalDateTime acceptedAt;

    @Builder
    private UserFriend(UserEntity user, UserEntity friend, LocalDateTime requestedAt) {
        this.user = user;
        this.friend = friend;
        this.requestedAt = requestedAt;
        this.isRequest = true;
    }

    public static UserFriend of(UserEntity user, LocalDateTime requestedAt) {
        return UserFriend.builder()
                .user(user)
                .requestedAt(requestedAt)
                .build();
    }

    public static UserFriend of(UserEntity user, UserEntity friend, LocalDateTime requestedAt){
        return UserFriend.builder()
                .user(user)
                .friend(friend)
                .requestedAt(requestedAt)
                .build();
    }

    public static UserFriend of(UserEntity user, UserEntity friend){
        UserFriend userFriend =  UserFriend.builder()
                .user(friend)
                .friend(user)
                .build();
        userFriend.accept();
        return userFriend;
    }

    public void accept(){
        this.isAccept = true;
        this.isRequest = false;
        this.acceptedAt = LocalDateTime.now();
    }

    public void inviteFriend(UserEntity friend){
        this.friend = friend;
    }


}
