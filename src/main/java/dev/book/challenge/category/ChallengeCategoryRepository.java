package dev.book.challenge.category;

import dev.book.challenge.ChallengeCategory;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeCategoryRepository extends JpaRepository<ChallengeCategory,Long> {
    List<ChallengeCategory> findByChallengeId(Long id);

    List<ChallengeCategory> findByCategoryIn(List<String> category);
}
