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
    private Long friendId;
    private String friendName;
    private String requestDate;

    public FriendResponseDto(Friendship friendship) {
        this.friendshipId = friendship.getFriendshipId();
        this.friendId = friendship.getUserId();  // 요청 보낸 사람 ID
        this.friendName = friendship.getRequester() != null ? friendship.getRequester().getUsername() : null;
        this.requestDate = friendship.getRequestDate().toString();
    }
}