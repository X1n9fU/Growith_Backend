package dev.book.util;

import dev.book.global.entity.Category;
import dev.book.user.entity.UserEntity;
import lombok.Getter;

import java.util.List;

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
    private static List<Category> categoryList
            = List.of(new Category("food", "음식"), new Category("cafe_snack", "카페 / 간식") );


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

    public static UserEntity withCategory(){
        UserEntity user = UserBuilder.of();
        user.updateCategory(categoryList);
        return user;
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
