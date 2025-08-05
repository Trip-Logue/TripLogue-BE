package com.triploguebe.friend.repository;

import com.triploguebe.friend.entity.Friendship;
import com.triploguebe.friend.entity.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByFriendshipIdAndStatus(Long friendshipId, FriendshipStatus status);

    List<Friendship> findAllByFriendIdAndStatus(Long FriendId, FriendshipStatus status);

    @Query("SELECT f FROM Friendship f WHERE " +
            "((f.requester.id = :userId1 AND f.receiver.id = :userId2) OR " +
            "(f.requester.id = :userId2 AND f.receiver.id = :userId1)) AND f.status = :status")
    Optional<Friendship> findFriendshipBetweenUsersWithStatus(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2,
            @Param("status") FriendshipStatus status
    );
}