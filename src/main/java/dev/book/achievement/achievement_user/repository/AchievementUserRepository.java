package dev.book.achievement.achievement_user.repository;

import dev.book.achievement.achievement_user.entity.AchievementUser;
import dev.book.achievement.entity.Achievement;
import dev.book.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementUserRepository extends JpaRepository<AchievementUser, Long> {
    boolean existsByAchievementAndUser(Achievement achievement, UserEntity user);
}
