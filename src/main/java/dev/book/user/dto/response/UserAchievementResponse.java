package dev.book.user.dto.response;

import java.time.LocalDateTime;

public record UserAchievementResponse(String title, String content, LocalDateTime createdAt) {
}
