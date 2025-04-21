package dev.book.user.user_friend.exception;

import dev.book.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserFriendErrorCode implements ErrorCode {
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "친구의 요청을 찾을 수 없습니다."),
    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "친구를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}