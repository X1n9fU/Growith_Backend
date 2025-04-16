package dev.book.global.config.security.entity;

import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    public RefreshToken(UserEntity user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }


    public void updateToken(String refreshToken) {
        this.refreshToken =refreshToken;
    }
}
