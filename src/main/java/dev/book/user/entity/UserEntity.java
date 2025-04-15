package dev.book.user.entity;

import dev.book.global.config.security.dto.OAuth2Attributes;
import dev.book.global.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String nickname;

    @NotNull
    private String profileImageUrl;

    private String userCategory; //추후 enum으로 변경

    private long savings = 0;

    private int completedChallenges = 0;

    private int participatingChallenges = 0;

    //todo 알림 설정 필요
//    private NotificationPreference notificationPreference;

    //todo 이후 entity과의 관계 설정 필요


    @Builder
    public UserEntity(String email, String name, String nickname, String profileImageUrl, String userCategory) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.userCategory = userCategory;
    }

    public static UserEntity of(OAuth2Attributes oAuth2Attributes){
        return UserEntity.builder()
                .email(oAuth2Attributes.email())
                .name(oAuth2Attributes.nickname())
                .nickname("")
                .profileImageUrl(oAuth2Attributes.profileImageUrl())
                .build();
    }
}
