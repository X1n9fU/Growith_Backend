package dev.book.accountbook.repository;

import dev.book.accountbook.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> , BudgetRepositoryCustom {
    Optional<Budget> findByIdAndUserId(Long id, Long userId);
}
