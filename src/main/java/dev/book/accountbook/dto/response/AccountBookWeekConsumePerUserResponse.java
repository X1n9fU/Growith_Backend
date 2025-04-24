package dev.book.accountbook.dto.response;

import dev.book.user.entity.UserEntity;

public record AccountBookWeekConsumePerUserResponse (UserEntity user, long lastWeekAmount, long twoWeeksAgoAmount) {
}
