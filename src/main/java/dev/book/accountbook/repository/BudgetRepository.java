package dev.book.accountbook.repository;

import dev.book.accountbook.entity.Budget;
import dev.book.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> , BudgetRepositoryCustom {
    Optional<Budget> findByIdAndUserId(Long id, Long userId);

    @Query("""
        SELECT b FROM Budget b JOIN FETCH UserEntity u WHERE b.month=:month
    """)
    List<Budget> findAllByMonthWithUser(@Param("month") int month);

    @Query("""
        SELECT DISTINCT u FROM UserEntity u LEFT JOIN Budget b ON u.id=b.user.id WHERE b.user.id IS NOT NULL AND b.month=:month 
    """)
    List<UserEntity> findUsersWithoutBudgetAtMonth(@Param("month") int month);
}
