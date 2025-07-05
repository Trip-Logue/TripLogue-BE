package com.triploguebe.friend.dto;

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
}