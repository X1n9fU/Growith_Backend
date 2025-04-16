package dev.book.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 챌린지를 찾을 수 없습니다."),
    CHALLENGE_INVALID(HttpStatus.FORBIDDEN, "수정 및 삭제 권한이 없습니다."),
    CHALLENGE_CAPACITY_FULL(HttpStatus.CONFLICT, "참여 인원이 초과 하였습니다."),
    CHALLENGE_ALREADY_JOINED(HttpStatus.CONFLICT, "이미 참여된 챌린지 입니다."),
    CHALLENGE_ALREADY_INVITED(HttpStatus.CONFLICT, "이미 초대된 요청입니다."),
    CHALLENGE_INVITE_INVALID(HttpStatus.CONFLICT, " 초대할 권한이 없습니다.");
    private final HttpStatus httpStatus;

    private final String message;
}
