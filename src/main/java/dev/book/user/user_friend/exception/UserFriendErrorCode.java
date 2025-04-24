package dev.book.user.user_friend.exception;

import dev.book.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserFriendErrorCode implements ErrorCode {

    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "친구 유저를 찾을 수 없습니다."),
    INVITING_NOT_FOUND(HttpStatus.NOT_FOUND, "초대 토큰과 알맞는 내역을 찾을 수 없습니다."),
    DUPLICATE_INVITATION(HttpStatus.CONFLICT, "이미 존재하는 초대 요청입니다."),
    ALREADY_MAKE_INVITATION(HttpStatus.BAD_REQUEST, "이미 초대 요청이 만들어진 상태입니다"),
    MYSELF_INVITATION(HttpStatus.BAD_REQUEST, "내가 나에게 초대 요청을 보낼 수 없습니다."),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "친구의 요청을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}