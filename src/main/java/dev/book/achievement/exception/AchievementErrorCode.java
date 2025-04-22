package dev.book.achievement.exception;

import dev.book.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AchievementErrorCode implements ErrorCode {
    ACHIEVEMENT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "등록된 업적 _id가 아닙니다."),
    INDIVIDUAL_ACHIEVEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "개인별 업적 등록 Entity를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
