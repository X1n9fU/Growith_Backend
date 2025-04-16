package dev.book.challenge.user_challenge.repository;

import dev.book.challenge.user_challenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    @Query("SELECT COUNT(u) FROM UserChallenge u WHERE u.challenge.id=:id")
    long countByChallengeId(Long id);

    boolean existsByUserIdAndChallengeId(Long id, Long id1);
}
