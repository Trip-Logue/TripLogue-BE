package com.triploguebe.friend.controller;

import com.triploguebe.friend.dto.FriendDecisionRequest;
import com.triploguebe.friend.dto.FriendRequestDto;
import com.triploguebe.friend.dto.FriendResponseDto;
import com.triploguebe.friend.service.FriendService;
import com.triploguebe.user.entity.User;
import com.triploguebe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    @PostMapping("/request")
    public void sendRequest(@RequestBody FriendRequestDto dto) {
        // TODO: 친구 요청 보내기
    }

    @PutMapping("/accept")
    public void accept(@RequestBody FriendDecisionRequest dto,
                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        friendService.acceptFriend(dto, user.getId());
    }

    @PutMapping("/reject")
    public void reject(@RequestBody FriendDecisionRequest dto,
                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        friendService.rejectFriend(dto, user.getId());
    }

    @GetMapping("/request/sent")
    public List<FriendResponseDto> getSentRequests(@RequestParam Long userId) {
        // TODO: 보낸 친구 요청 목록 조회
        return Collections.emptyList();
    }

    @GetMapping("/request/received")
    public List<FriendResponseDto> getReceivedRequests(@RequestParam Long userId) {
        // TODO: 받은 친구 요청 목록 조회
        return Collections.emptyList();
    }

    @GetMapping("/friends")
    public List<FriendResponseDto> getFriends(@RequestParam Long userId) {
        // TODO: 친구 목록 조회
        return Collections.emptyList();
    }
}