package com.triploguebe.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String profileImageUrl;
    private String token;
}

