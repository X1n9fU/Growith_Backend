package dev.book.user_friend.repository;

import dev.book.user_friend.entity.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {

    @Query("SELECT uf FROM UserFriend uf JOIN FETCH uf.user n where n.id=:id and uf.requestedAt=:requestAt")
    Optional<UserFriend> findByInvitingUserAndRequestedAt(@Param("id")Long userId, @Param("requestAt")LocalDateTime requestAt);

}
