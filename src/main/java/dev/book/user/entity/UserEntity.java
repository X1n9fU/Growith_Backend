package dev.book.user.entity;

import dev.book.achievement.achievement_user.entity.AchievementUser;
import dev.book.challenge.user_challenge.entity.UserChallenge;
import dev.book.global.config.security.dto.oauth2.OAuth2Attributes;
import dev.book.global.entity.BaseTimeEntity;
import dev.book.global.entity.Category;
import dev.book.user.user_category.UserCategory;
import dev.book.user.user_friend.entity.UserFriend;
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

    private long savings = 0;

    private int completedChallenges = 0;

    private int participatingChallenges = 0;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCategory> userCategory = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFriend> sendFriendRequests = new ArrayList<>(); //유저가 친구 요청 보냄

    @OneToMany(mappedBy = "friend", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFriend> receivedFriendRequests = new ArrayList<>(); //유저가 친구 요청 받음

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChallenge> userChallenges = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AchievementUser> achievements = new ArrayList<>();

    //todo 알림 설정 필요
    //todo 이후 entity과의 관계 설정 필요


    @Builder
    public UserEntity(String email, String name, String nickname, String profileImageUrl) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserEntity of(OAuth2Attributes oAuth2Attributes) {
        return UserEntity.builder()
                .email(oAuth2Attributes.email())
                .name(oAuth2Attributes.nickname())
                .nickname("")
                .profileImageUrl(oAuth2Attributes.profileImageUrl())
                .build();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateCategory(List<Category> categories) {
        if (!this.userCategory.isEmpty())
            this.userCategory.clear();

        List<UserCategory> userCategories = getUserCategories(categories);
        for (UserCategory uc : userCategories) {
            uc.setUser(this);
            this.userCategory.add(uc);
        }
    }

    private List<UserCategory> getUserCategories(List<Category> categories) {
        List<UserCategory> userCategories = new ArrayList<>();
        for (Category category : categories) {
            UserCategory userCategory = new UserCategory(this, category);
            userCategories.add(userCategory);
        }
        return userCategories;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void deleteNickname() { this.nickname = ""; }

    public void plusChallengeCount() {
        this.participatingChallenges++;
    }

    public void minusChallengeCount() {
        this.participatingChallenges--;
    }

}
