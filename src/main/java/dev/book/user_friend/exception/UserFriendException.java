package dev.book.user_friend.exception;

import dev.book.global.exception.CustomErrorException;
import lombok.Getter;

@Getter
public class UserFriendException extends CustomErrorException {

    private final UserFriendErrorCode errorCode;

    public UserFriendException(UserFriendErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
        this.errorCode = errorCode;
    }
}
