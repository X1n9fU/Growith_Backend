package dev.book.util;

import dev.book.user.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserBuilder {

    private static final String EMAIL = "test@test.com";
    private static final String NAME = "test";
    private static final String NICKNAME = "test";
    private static final String PROFILE_IMAGE_URL = "Image";

    private static String email = EMAIL;
    private static String name = NAME;
    private static String nickname = NICKNAME;
    private static String profileImageUrl = PROFILE_IMAGE_URL;

    public static UserEntity of() {
        return UserEntity.builder()
                .email(email)
                .nickname(nickname)
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();
    }
    public static UserEntity of(String email){
        return UserEntity.builder()
                .email(email)
                .nickname(nickname)
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public static UserEntity of(String email, String name){
        return UserEntity.builder()
                .email(email)
                .nickname(nickname)
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public static UserEntity newUser(String email, String name){
        return UserEntity.builder()
                .email(email)
                .nickname("")
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
