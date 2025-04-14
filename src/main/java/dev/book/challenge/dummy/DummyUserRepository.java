package dev.book.challenge.dummy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyUserRepository extends JpaRepository<DummyUser, Long> {
}
