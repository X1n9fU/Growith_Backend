package dev.book.challenge.repository;

import dev.book.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeJpaRepositiory {
}
