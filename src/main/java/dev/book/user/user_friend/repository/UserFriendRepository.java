package dev.book.user.user_friend.repository;

import dev.book.user.entity.UserEntity;
import dev.book.user.user_friend.entity.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {

    @Query("SELECT uf FROM UserFriend uf JOIN uf.user n where n.id=:id and uf.requestedAt=:requestAt")
    Optional<UserFriend> findByInvitingUserAndRequestedAt(@Param("id")Long userId, @Param("requestAt")LocalDateTime requestAt);

    @Query("SELECT uf.user FROM UserFriend uf JOIN uf.user n WHERE uf.friend.id=:id AND uf.isRequest=true")
    List<UserEntity> findAllByInvitedUserAndIsRequestIsTrue(@Param("id") Long userId);

    @Query("SELECT uf.user FROM UserFriend uf JOIN uf.user n WHERE uf.friend.id=:id AND uf.isAccept=true")
    List<UserEntity> findAllByInvitedUserAndIsAcceptIsTrue(@Param("id") Long userId);


    Optional<UserFriend> findByUserAndFriendAndIsRequestIsTrue(UserEntity user, UserEntity friend);

    @Modifying
    void deleteByUserAndFriendAndIsAcceptIsTrue(UserEntity user, UserEntity friend);

    boolean existsByUserAndRequestedAt(UserEntity invitingUser, LocalDateTime localDateTime);
}
