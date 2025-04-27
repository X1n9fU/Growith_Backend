package dev.book.tip.repository;

import dev.book.tip.entity.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipRepository extends JpaRepository<Tip, Long> {

    @Query("SELECT t FROM Tip t ORDER BY FUNCTION('RAND') LIMIT 20")
    List<Tip> find20RandomTips();
}
