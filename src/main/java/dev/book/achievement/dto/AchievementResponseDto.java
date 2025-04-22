package dev.book.achievement.dto;

import dev.book.achievement.entity.Achievement;
import lombok.Builder;

@Builder
public record AchievementResponseDto (String title, String content) {

    public static AchievementResponseDto from(Achievement achievement){
        return AchievementResponseDto.builder()
                .title(achievement.getTitle())
                .content(achievement.getContent())
                .build();
    }
}
