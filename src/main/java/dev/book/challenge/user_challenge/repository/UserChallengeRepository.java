package dev.book.challenge.user_challenge.repository;

import dev.book.challenge.user_challenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    @Query("SELECT COUNT(u) FROM UserChallenge u WHERE u.challenge.id=:id")
    long countByChallengeId(Long id);

    boolean existsByUserIdAndChallengeId(Long id, Long challengeId);

    void deleteByUserIdAndChallengeId(Long userId, Long challengeId);

    List<UserChallenge> findByChallengeId(Long challengeId);

    @Query("SELECT uc.user.id FROM UserChallenge uc WHERE uc.challenge.id=:challengeId")
    List<Long> findUserIdByChallengeId(Long challengeId);
}
