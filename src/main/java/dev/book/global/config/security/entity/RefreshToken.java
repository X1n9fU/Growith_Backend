package dev.book.global.config.security.entity;

import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne @Cascade(CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String refreshToken;

    public RefreshToken(UserEntity user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
