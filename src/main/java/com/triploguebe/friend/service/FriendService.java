package com.triploguebe.friend.service;

import com.triploguebe.friend.dto.FriendDecisionRequest;
import com.triploguebe.friend.dto.FriendRequestDto;
import com.triploguebe.friend.dto.FriendResponseDto;
import com.triploguebe.friend.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendshipRepository friendshipRepository;

    public void sendFriendRequest(FriendRequestDto dto) {
        // TODO: 친구 요청 저장
    }

    public void acceptFriend(FriendDecisionRequest dto) {
        // TODO: 요청 수락
    }

    public void rejectFriend(FriendDecisionRequest dto) {
        // TODO: 요청 거절
    }

    public List<FriendResponseDto> getSentRequests(Long userId) {
        // TODO: 내가 보낸 요청 조회
        return null;
    }

    public List<FriendResponseDto> getReceivedRequests(Long userId) {
        // TODO: 내가 받은 요청 조회
        return null;
    }

    public List<FriendResponseDto> getFriends(Long userId) {
        // TODO: 내 친구 목록 조회
        return null;
    }
}
