package com.triploguebe.friend.repository;

import com.triploguebe.friend.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUserIdAndFriendIdAndStatusIn(Long userId, Long friendId, List<Friendship.Status> status);

    List<Friendship> findByUserIdAndStatus(Long userId, Friendship.Status status);

    List<Friendship> findByUserIdOrFriendIdAndStatus(Long userId, Long friendId, Friendship.Status status);
}
