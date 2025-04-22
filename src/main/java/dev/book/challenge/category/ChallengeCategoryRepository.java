package dev.book.challenge.category;

import dev.book.challenge.ChallengeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeCategoryRepository extends JpaRepository<ChallengeCategory, Long> {
}
