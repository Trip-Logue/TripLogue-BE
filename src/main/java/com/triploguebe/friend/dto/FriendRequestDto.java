package com.triploguebe.friend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {
    private Long userId;    // 친구 요청을 보낸 사람 ID
    private Long friendId;  // 친구 요청 받을 사람 ID
}

