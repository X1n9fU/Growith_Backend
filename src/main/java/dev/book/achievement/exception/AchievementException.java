package dev.book.achievement.exception;

import dev.book.global.exception.CustomErrorException;
import lombok.Getter;

@Getter
public class AchievementException extends CustomErrorException {

    private final AchievementErrorCode achievementErrorCode;
    public AchievementException(AchievementErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
        achievementErrorCode = errorCode;
    }
}
