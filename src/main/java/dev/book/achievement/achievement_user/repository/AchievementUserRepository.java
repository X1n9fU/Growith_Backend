package dev.book.achievement.achievement_user.repository;

import dev.book.achievement.achievement_user.entity.AchievementUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementUserRepository extends JpaRepository<AchievementUser, Long> {
}
