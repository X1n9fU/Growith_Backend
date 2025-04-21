package dev.book.challenge.repository;

import dev.book.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeJpaRepository {

    @Query("SELECT c FROM Challenge c JOIN FETCH c.creator u where c.id=:id")
    Optional<Challenge> findWithCreatorById(Long id);

    @Query("SELECT c FROM Challenge c JOIN FETCH c.creator u where c.id=:id and u.id=:creatorId")
    Optional<Challenge> findByIdAndCreatorId(Long id, Long creatorId);

    @Modifying
    @Query("UPDATE Challenge c SET c.status='COMPLETED' WHERE c.endDate<:today")
    void updateChallengeStatusByDate(@Param("today") LocalDate today);


    @Query("SELECT c FROM Challenge c WHERE c.startDate <= :date AND c.endDate >= :date")
    List<Challenge> findAllByCategoryAndDate(@Param("date") LocalDate date);
}
