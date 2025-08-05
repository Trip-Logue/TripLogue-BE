package com.triploguebe.friend.service;

import com.triploguebe.friend.dto.FriendRequestDto;
import com.triploguebe.friend.dto.FriendResponseDto;
import com.triploguebe.friend.entity.Friendship;
import com.triploguebe.friend.repository.FriendshipRepository;
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
                Arrays.asList(Friendship.Status.PENDING, Friendship.Status.ACCEPTED)).isPresent()
                ||
                friendshipRepository.findByUserIdAndFriendIdAndStatusIn(
                        dto.getFriendId(), dto.getUserId(),
                        Arrays.asList(Friendship.Status.PENDING, Friendship.Status.ACCEPTED)).isPresent();
        if (exists) {
            throw new IllegalArgumentException("이미 해당 사용자에게 친구 요청을 했거나 친구인 사용자입니다.");
        }
        Friendship friendship = Friendship.builder()
                .userId(dto.getUserId())
                .friendId(dto.getFriendId())
                .status(Friendship.Status.PENDING)
                .requestDate(LocalDateTime.now())
                .build();
        friendshipRepository.save(friendship);
    }

    //보낸 친구 요청 목록
    public List<FriendResponseDto> getSentRequests(Long userId) {
        List<Friendship> sent = friendshipRepository.findByUserIdAndStatus(userId, Friendship.Status.PENDING);
        return sent.stream().map(f -> toResponseDto(f, userId)).collect(Collectors.toList());
    }

    //친구 목록
    public List<FriendResponseDto> getFriends(Long userId) {
        List<Friendship> friends = friendshipRepository.findByUserIdOrFriendIdAndStatus(
                userId, userId, Friendship.Status.ACCEPTED);
        return friends.stream().map(f -> toResponseDto(f, userId)).collect(Collectors.toList());
    }

    public FriendResponseDto getFriend(Long friendshipId) {
        Friendship f = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("친구 정보를 찾을 수 없습니다."));
        return toResponseDto(f, null);
    }

    //친구 삭제
    public void deleteFriend(Long friendshipId, Long userId) {
        Friendship f = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("친구 정보를 찾을 수 없습니다."));
        if (!f.getUserId().equals(userId) && !f.getFriendId().equals(userId)) {
            throw new IllegalArgumentException("자신의 친구 관계만 삭제할 수 있습니다.");
        }
        f.setStatus(Friendship.Status.DELETED);
        friendshipRepository.save(f);
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
                .friendId(otherUserId)
                .friendName(friendName)
                .requestDate(f.getRequestDate() != null ? f.getRequestDate().toString() : null)
                .build();
    }
}
