package dev.book.accountbook.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountBookErrorCode implements ErrorCode {
    NOT_FOUND_SPEND(HttpStatus.BAD_REQUEST, "존재하지 않는 소비내역입니다."),
    NOT_FOUND_INCOME(HttpStatus.BAD_REQUEST, "존재하지 않는 수입내역입니다.");

    private final HttpStatus status;
    private final String message;
}
