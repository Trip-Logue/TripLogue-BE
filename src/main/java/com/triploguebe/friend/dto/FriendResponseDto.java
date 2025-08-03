package com.triploguebe.friend.dto;

import com.triploguebe.friend.entity.Friendship;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendResponseDto {
    private Long friendshipId;
    private Long requesterId;         // 요청 보낸 사람 ID
    private String requesterName;     // 요청 보낸 사람 이름
    private String requestDate;

    public FriendResponseDto(Friendship friendship) {
        this.friendshipId = friendship.getFriendshipId();
        this.requesterId = friendship.getUserId(); // 요청 보낸 사람의 ID
        this.requesterName = friendship.getRequester() != null ? friendship.getRequester().getUsername() : null;
        this.requestDate = friendship.getRequestDate() != null ? friendship.getRequestDate().toString() : null;
    }
}
