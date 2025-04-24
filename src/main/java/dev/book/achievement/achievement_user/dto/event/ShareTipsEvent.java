package dev.book.achievement.achievement_user.dto.event;

import dev.book.user.entity.UserEntity;

public record ShareTipsEvent(UserEntity user){
}
