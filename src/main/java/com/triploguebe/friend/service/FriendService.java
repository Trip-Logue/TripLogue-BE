package com.triploguebe.friend.service;

import com.triploguebe.friend.dto.FriendDecisionRequest;
import com.triploguebe.friend.dto.FriendRequestDto;
import com.triploguebe.friend.dto.FriendResponseDto;
import com.triploguebe.friend.entity.Friendship;
import com.triploguebe.friend.entity.FriendshipStatus;
import com.triploguebe.friend.repository.FriendshipRepository;
import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.user.entity.User;
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

    public void acceptFriend(FriendDecisionRequest dto, Long userId) {
        Friendship friendship = findPendingFriendshipOrThrow(dto.getRequestId());
        validateRequestReceiver(friendship, userId);

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
    }

    public void rejectFriend(FriendDecisionRequest dto, Long userId) {
        Friendship friendship = findPendingFriendshipOrThrow(dto.getRequestId());
        validateRequestReceiver(friendship, userId);

        friendship.setStatus(FriendshipStatus.REJECTED);
        friendshipRepository.save(friendship);
    }

    public List<FriendResponseDto> getSentRequests(Long userId) {
        // TODO: 내가 보낸 요청 조회
        return null;
    }

    public List<FriendResponseDto> getReceivedRequests(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllByFriendIdAndStatus(userId, FriendshipStatus.PENDING);
        return friendships.stream()
                .map(friendship -> {
                    User sender = friendship.getRequester(); // 요청 보낸 사람
                    return new FriendResponseDto(
                            friendship.getFriendshipId(),
                            sender.getId(),
                            sender.getUsername(),
                            friendship.getRequestDate().toString()
                    );
                })
                .toList();
    }

    public List<FriendResponseDto> getFriends(Long userId) {
        // TODO: 내 친구 목록 조회
        return null;
    }

    // 요청 상태가 PENDING인 경우
    private Friendship findPendingFriendshipOrThrow(Long requestId) {
        return friendshipRepository.findByIdAndStatus(requestId, FriendshipStatus.PENDING)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIENDSHIP_NOT_FOUND));
    }

    // 요청 수신자인지 검증
    private void validateRequestReceiver(Friendship friendship, Long userId) {
        if (!friendship.getFriendId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
