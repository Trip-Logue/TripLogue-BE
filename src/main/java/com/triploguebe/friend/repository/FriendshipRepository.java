package com.triploguebe.friend.repository;

import com.triploguebe.friend.entity.Friendship;
import com.triploguebe.friend.entity.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // 친구 요청 수락, 거절할 때 해당 요청이 존재하는지, 아직 PENDING 상태인지 확인
    Optional<Friendship> findByIdAndStatus(Long id, FriendshipStatus status);

    // 내가 받은 요청 중 아직 대기 중인(PENDING) 요청들 조회
    List<Friendship> findAllByFriendIdAndStatus(Long FriendId, FriendshipStatus status);

}