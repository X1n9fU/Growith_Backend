package dev.book.user.entity;

import dev.book.accountbook.type.Category;
import dev.book.global.config.security.dto.oauth2.OAuth2Attributes;
import dev.book.global.config.security.entity.RefreshToken;
import dev.book.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private String profileImageUrl; //null인 경우 프로필 없음

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Category> userCategory = new ArrayList<>(); //추후 enum으로 변경

    private long savings = 0;

    private int completedChallenges = 0;

    private int participatingChallenges = 0;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    //todo 알림 설정 필요
//    private NotificationPreference notificationPreference;

    //todo 이후 entity과의 관계 설정 필요


    @Builder
    public UserEntity(String email, String name, String nickname, String profileImageUrl, List<Category> userCategory) {
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

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateCategory(List<Category> userCategory){
        this.userCategory = userCategory;
    }

    public void updateProfileImage(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void updateRefreshToken(RefreshToken refreshTokenEntity) {
        this.refreshToken = refreshTokenEntity;
    }
    public void deleteRefreshToken() {this.refreshToken = null;}
}
