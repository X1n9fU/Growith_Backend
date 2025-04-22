package dev.book.achievement.dto.event;

import dev.book.achievement.entity.Achievement;

public record GetAchievementEvent(Achievement achievement, Long userId) {
}
