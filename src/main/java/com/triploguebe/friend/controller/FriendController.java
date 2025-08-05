package com.triploguebe.friend.controller;

import com.triploguebe.friend.dto.FriendDecisionRequest;
import com.triploguebe.friend.dto.FriendDetailResponseDto;
import com.triploguebe.friend.dto.FriendRequestDto;
import com.triploguebe.friend.dto.FriendResponseDto;
import com.triploguebe.friend.service.FriendService;
import com.triploguebe.user.entity.User;
import com.triploguebe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    //친구 요청
    @PostMapping("/request")
    public void sendRequest(@RequestBody FriendRequestDto dto) {
        friendService.sendFriendRequest(dto);
    }

    @PutMapping("/accept")
    public ResponseEntity<Map<String, String>> accept(@RequestBody FriendDecisionRequest dto,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        friendService.acceptFriend(dto, user.getId());

        Map<String, String> body = new HashMap<>();
        body.put("message", "친구 요청을 수락했습니다.");
        return ResponseEntity.ok(body);
    }

    @PutMapping("/reject")
    public ResponseEntity<Map<String, String>> reject(@RequestBody FriendDecisionRequest dto,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        friendService.rejectFriend(dto, user.getId());

        Map<String, String> body = new HashMap<>();
        body.put("message", "친구 요청을 거절했습니다.");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{friendId}")
    public ResponseEntity<FriendDetailResponseDto> getFriendDetail(
            @PathVariable Long friendId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        Long currentUserId = user.getId();
        FriendDetailResponseDto friendDetail = friendService.getFriendDetail(currentUserId, friendId);
        return ResponseEntity.ok(friendDetail);
    }

    //보낸 친구 요청 목록
    @GetMapping("/request/sent")
    public List<FriendResponseDto> getSentRequests(@RequestParam Long userId) {
        return friendService.getSentRequests(userId);
        // TODO: 보낸 친구 요청 목록 조회
        return Collections.emptyList();
    }

    @GetMapping("/request/received")
    public List<FriendResponseDto> getReceivedRequests(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Long userId = user.getId();
        return friendService.getReceivedRequests(userId);
    }

    //친구 목록
    @GetMapping("/friends")
    public List<FriendResponseDto> getFriends(@RequestParam Long userId) {
        return friendService.getFriends(userId);
    }

    //친구 정보
    @GetMapping("/{friendshipId}")
    public FriendResponseDto getFriend(@PathVariable Long friendshipId) {
        return friendService.getFriend(friendshipId);
    }

    //친구 삭제
    @DeleteMapping("/{friendshipId}")
    public void deleteFriend(@PathVariable Long friendshipId, @RequestParam Long userId) {
        friendService.deleteFriend(friendshipId, userId);
    }
}
