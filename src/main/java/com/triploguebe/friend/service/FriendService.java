package com.triploguebe.friend.service;

import com.triploguebe.friend.dto.FriendRequestDto;
import com.triploguebe.friend.dto.FriendResponseDto;
import com.triploguebe.friend.entity.Friendship;
import com.triploguebe.friend.dto.FriendDecisionRequest;
import com.triploguebe.friend.dto.FriendDetailResponseDto;
import com.triploguebe.friend.entity.FriendshipStatus;
import com.triploguebe.friend.repository.FriendshipRepository;
import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.user.entity.User;
import com.triploguebe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;  // UserRepository 추가

    //친구 요청
    public void sendFriendRequest(FriendRequestDto dto) {
        if (dto.getUserId().equals(dto.getFriendId())) {
            throw new IllegalArgumentException("자신에게 친구 요청을 보낼 수 없습니다.");
        }
        boolean exists = friendshipRepository.findByUserIdAndFriendIdAndStatusIn(
                dto.getUserId(), dto.getFriendId(),
                Arrays.asList(FriendshipStatus.PENDING, FriendshipStatus.ACCEPTED)).isPresent()
                ||
                friendshipRepository.findByUserIdAndFriendIdAndStatusIn(
                        dto.getFriendId(), dto.getUserId(),
                        Arrays.asList(FriendshipStatus.PENDING, FriendshipStatus.ACCEPTED)).isPresent();
        if (exists) {
            throw new IllegalArgumentException("이미 해당 사용자에게 친구 요청을 했거나 친구인 사용자입니다.");
        }
        Friendship friendship = Friendship.builder()
                .userId(dto.getUserId())
                .friendId(dto.getFriendId())
                .status(FriendshipStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .build();
        friendshipRepository.save(friendship);
    }

    //보낸 친구 요청 목록
    public List<FriendResponseDto> getSentRequests(Long userId) {
        List<Friendship> sent = friendshipRepository.findByUserIdAndStatus(userId, FriendshipStatus.PENDING);
        return sent.stream().map(f -> toResponseDto(f, userId)).collect(Collectors.toList());
    }

    //친구 목록
    public List<FriendResponseDto> getFriends(Long userId) {
        List<Friendship> friends = friendshipRepository.findFriendsByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED);
        return friends.stream()
                .map(f -> toResponseDto(f, userId))
                .collect(Collectors.toList());
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

    //친구 삭제
    public void deleteFriend(Long friendshipId, Long userId) {
        Friendship f = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("친구 정보를 찾을 수 없습니다."));
        if (!f.getUserId().equals(userId) && !f.getFriendId().equals(userId)) {
            throw new IllegalArgumentException("자신의 친구 관계만 삭제할 수 있습니다.");
        }
        f.setStatus(FriendshipStatus.DELETED);
        friendshipRepository.save(f);
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

    public FriendDetailResponseDto getFriendDetail(Long requesterId, Long targetUserId) {
        Friendship friendship = friendshipRepository
                .findFriendshipBetweenUsersWithStatus(requesterId, targetUserId, FriendshipStatus.ACCEPTED)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));

        User friend = friendship.getReceiver();

        return new FriendDetailResponseDto(
                friend.getId(),
                friend.getUsername(),
                friend.getProfileImageUrl()
        );
    }

    private FriendResponseDto toResponseDto(Friendship f, Long currentUserId) {
        Long otherUserId = null;
        if (currentUserId != null) {
            otherUserId = f.getUserId().equals(currentUserId) ? f.getFriendId() : f.getUserId();
        } else {
            otherUserId = f.getFriendId();
        }

        User otherUser = userRepository.findById(otherUserId).orElse(null);
        String friendName = (otherUser != null) ? otherUser.getUsername() : null;

        return FriendResponseDto.builder()
                .friendshipId(f.getFriendshipId())
                .requesterId(otherUserId)
                .requesterName(friendName)
                .requestDate(f.getRequestDate() != null ? f.getRequestDate().toString() : null)
                .build();
    }

    private Friendship findPendingFriendshipOrThrow(Long requestId) {
        return friendshipRepository.findByFriendshipIdAndStatus(requestId, FriendshipStatus.PENDING)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIENDSHIP_NOT_FOUND));
    }

    private void validateRequestReceiver(Friendship friendship, Long userId) {
        if (!friendship.getFriendId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
