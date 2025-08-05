package com.triploguebe.friend.controller;

import com.triploguebe.friend.dto.FriendRequestDto;
import com.triploguebe.friend.dto.FriendResponseDto;
import com.triploguebe.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    //친구 요청
    @PostMapping("/request")
    public void sendRequest(@RequestBody FriendRequestDto dto) {
        friendService.sendFriendRequest(dto);
    }

    //보낸 친구 요청 목록
    @GetMapping("/request/sent")
    public List<FriendResponseDto> getSentRequests(@RequestParam Long userId) {
        return friendService.getSentRequests(userId);
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
