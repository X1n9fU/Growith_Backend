package dev.book.global.config.security.repository;

import dev.book.global.config.security.entity.RefreshToken;
import dev.book.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(UserEntity user);

    void deleteByUser(UserEntity user);
}
