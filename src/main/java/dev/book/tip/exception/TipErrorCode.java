package dev.book.tip.exception;

import dev.book.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TipErrorCode implements ErrorCode {

    ALREADY_EXISTED(HttpStatus.CONFLICT, "이미 팁이 작성된 챌린지입니다.");

    private final HttpStatus status;

    private final String message;

}
