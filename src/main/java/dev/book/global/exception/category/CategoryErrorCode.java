package dev.book.global.exception.category;

import dev.book.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {

    CATEGORY_BAD_REQUEST(HttpStatus.BAD_REQUEST, "일치하지 않는 카테고리가 존재합니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리 입니다.");

    private final HttpStatus status;
    private final String message;
}
