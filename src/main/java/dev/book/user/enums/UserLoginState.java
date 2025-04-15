package dev.book.user.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserLoginState {
    LOGIN_SUCCESS("로그인 성공"),
    PROFILE_INCOMPLETE("프로필 미설정");

    private final String description;
}
