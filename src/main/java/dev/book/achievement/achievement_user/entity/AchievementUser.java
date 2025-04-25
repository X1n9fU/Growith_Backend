package dev.book.achievement.achievement_user.entity;

import dev.book.achievement.entity.Achievement;
import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class AchievementUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="achievement_id")
    private Achievement achievement;

    @CreatedDate
    private LocalDateTime createdAt;

    public AchievementUser(UserEntity user, Achievement achievement) {
        this.user = user;
        this.achievement = achievement;
    }
}
