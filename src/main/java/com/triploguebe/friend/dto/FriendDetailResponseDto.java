package com.triploguebe.friend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendDetailResponseDto {
    @JsonProperty("friend_id")
    private Long friendId;

    @JsonProperty("friend_name")
    private String friendName;

    @JsonProperty("profile_image")
    private String profileImage;
}

