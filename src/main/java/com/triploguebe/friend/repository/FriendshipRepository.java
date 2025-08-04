package com.triploguebe.friend.repository;

import com.triploguebe.friend.entity.Friendship;
import com.triploguebe.friend.entity.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // 친구 요청 수락, 거절할 때 해당 요청이 존재하는지, 아직 PENDING 상태인지 확인
    Optional<Friendship> findByFriendshipIdAndStatus(Long friendshipId, FriendshipStatus status);

    // 내가 받은 요청 중 아직 대기 중인(PENDING) 요청들 조회
    List<Friendship> findAllByFriendIdAndStatus(Long FriendId, FriendshipStatus status);

    // 친구 관계가 상호적인지 확인
    @Query("SELECT f FROM Friendship f WHERE " +
            "((f.requester.id = :userId1 AND f.receiver.id = :userId2) OR " +
            "(f.requester.id = :userId2 AND f.receiver.id = :userId1)) AND f.status = :status")
    Optional<Friendship> findFriendshipBetweenUsersWithStatus(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2,
            @Param("status") FriendshipStatus status
    );
}