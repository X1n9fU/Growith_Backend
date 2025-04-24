package dev.book.user.repository;

import dev.book.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("""
        SELECT u FROM UserEntity u
        JOIN FETCH u.userCategory uc
        JOIN FETCH uc.category
        WHERE u.email = :email
    """)
    Optional<UserEntity> findByEmailWithCategories(@Param("email") String email);

    boolean existsByNickname(String nickname);
}
