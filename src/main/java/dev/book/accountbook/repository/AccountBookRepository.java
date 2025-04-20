package dev.book.accountbook.repository;

import dev.book.accountbook.dto.response.AccountBookStatResponse;
import dev.book.accountbook.entity.AccountBook;
import dev.book.accountbook.type.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {
    Optional<AccountBook> findByIdAndUserId(Long id, Long userId);

    List<AccountBook> findAllByUserIdAndTypeOrderByUpdatedAtDesc(Long userId, CategoryType type);

    @Query("""
                SELECT new dev.book.accountbook.dto.response.AccountBookStatResponse(c.korean, SUM(ab.amount))
                FROM AccountBook ab
                JOIN ab.categoryList abc
                JOIN abc.category c
                WHERE ab.user.id = :userId
                  AND ab.updatedAt BETWEEN :startDate AND :endDate
                GROUP BY c
                ORDER BY SUM(ab.amount) DESC
            """)
    List<AccountBookStatResponse> findTopCategoriesByUserAndPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"categoryList", "categoryList.category"})
    @Query("""
                SELECT a
                FROM AccountBook a
                JOIN a.categoryList abc
                JOIN abc.category c
                WHERE a.user.id = :userId
                  AND a.type = :categoryType
                  AND c.category = :categoryName
                  AND a.updatedAt BETWEEN :startDate AND :endDate
                ORDER BY a.updatedAt DESC
            """)
    List<AccountBook> findByCategory(
            @Param("userId") Long userId,
            @Param("categoryType") CategoryType categoryType,
            @Param("categoryName") String categoryName,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
                SELECT COALESCE(SUM(a.amount), 0)
                FROM AccountBook a
                JOIN a.categoryList abc
                JOIN abc.category c
                WHERE a.user.id = :userId
                  AND a.type = :categoryType
                  AND c.category = :categoryName
                  AND a.updatedAt BETWEEN :startDate AND :endDate
            """)
    Integer sumSpending(
            @Param("userId") Long userId,
            @Param("categoryType") CategoryType categoryType,
            @Param("categoryName") String categoryName,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @EntityGraph(attributePaths = {"categoryList", "categoryList.category"})
    @Query("""
                SELECT DISTINCT ab
                FROM AccountBook ab
                JOIN ab.categoryList abc
                JOIN abc.category c
                WHERE ab.user.id = :userId
                  AND c.korean = :categoryName
                ORDER BY ab.updatedAt DESC, ab.id DESC
            """)
    Page<AccountBook> findByUserIdAndCategoryNameWithGraph(
            @Param("userId") Long userId,
            @Param("categoryName") String categoryName,
            Pageable pageable
    );
}
