package dev.book.challenge.challenge_invite.repository;

import dev.book.challenge.challenge_invite.entity.ChallengeInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeInviteRepository extends JpaRepository<ChallengeInvite, Long> {
    boolean existsByInviteUserIdAndChallengeId(Long inviteUserId, Long challengeId);
}
