package com.triploguebe.friend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendDecisionRequest {
    private Long requestId; // 요청 보낸 사람의 user_id
}