package dev.book.global.config.Firebase.dto;

public record LimitWarningFcmEvent (Long userId, String nickname, int budget, long total, long usageRate){
}
